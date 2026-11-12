#!/usr/bin/env python3
"""Compare original and current config code against old-file fixtures, without Minecraft."""
from pathlib import Path
import os
import shutil
import subprocess
import uuid

ROOT = Path(__file__).resolve().parents[1]
PACKAGE = 'common/src/main/java/net/vg/sleepcycle/config'
STUBS = {
    'dev/architectury/platform/Platform.java': '''package dev.architectury.platform;
public class Platform { public static java.nio.file.Path getConfigFolder() {
return java.nio.file.Path.of(System.getProperty("config.folder")); } }''',
    'net/vg/sleepcycle/Constants.java': '''package net.vg.sleepcycle;
public class Constants { public static final String MOD_ID="sleepcycle", MOD_NAME="Sleep Cycle",
MOD_VERSION=System.getProperty("config.version"); }''',
    'net/vg/sleepcycle/SleepCycle.java': 'package net.vg.sleepcycle; public class SleepCycle {}',
    'org/apache/logging/log4j/Logger.java': '''package org.apache.logging.log4j;
public class Logger { public void info(Object s) {} public void warn(Object s) {}
public void error(Object s) {} public void trace(Object s) {} }''',
    'org/apache/logging/log4j/LogManager.java': '''package org.apache.logging.log4j;
public class LogManager { public static Logger getLogger(String s) { return new Logger(); } }''',
    'com/mojang/datafixers/util/Pair.java': '''package com.mojang.datafixers.util;
public class Pair<A,B> { private A a; private B b; public Pair(A a,B b) {this.a=a;this.b=b;}
public A getFirst(){return a;} public B getSecond(){return b;} }''',
}
HARNESS = r'''import java.nio.file.*;
import java.util.*;
import net.vg.sleepcycle.config.*;
public class ConfigRoundTrip {
    static void check(boolean v, String m) { if (!v) throw new AssertionError(m); }
    static Map<String,String> entries(String text) {
        Map<String,String> result=new LinkedHashMap<>();
        text.lines().filter(l -> !l.startsWith("#") && l.contains("=")).forEach(l -> {
            String[] p=l.split("=",2); result.put(p[0],p[1]); }); return result;
    }
    public static void main(String[] args) throws Exception {
        boolean current=args[0].equals("current");
        ModConfigs.registerConfigs();
        Path file=Path.of(System.getProperty("config.folder"),"sleepcycleconfig-"+System.getProperty("config.version")+".properties");
        String defaults=Files.readString(file);
        check(entries(defaults).size()==13,"Default key count changed");
        Files.writeString(file.resolveSibling("defaults.txt"),defaults);
        String custom=defaults.replace("sleep.allow.day.sleep=true","sleep.allow.day.sleep=false")
            .replace("sleep.day.skip.speed=60.0","sleep.day.skip.speed=73.5")
            .replace("sleep.well.rested.wait=240","sleep.well.rested.wait=277")
            .replace("sleep.sleep.button.height=30","sleep.sleep.button.height=37")
            +"\n# Keep my custom comment\nunknown.future.key=keep=everything\n";
        if(current) custom += "# Caf\u00e9 \u7761\u7720\nunknown.unicode=\u00e9\u7761\u7720\n";
        Files.writeString(file,custom);
        ModConfigs.registerConfigs();
        check(!ModConfigs.ALLOW_DAY_SLEEPING && ModConfigs.DAY_SKIP_SPEED==73.5 &&
            ModConfigs.WELL_RESTED_WAIT==277 && ModConfigs.SLEEP_BUTTON_HEIGHT==37,"Custom values lost on load");
        ModConfigs.saveConfigs(); ModConfigs.registerConfigs();
        String saved=Files.readString(file);
        check(entries(custom).equals(entries(saved)),"Round trip changed a value/unknown key");
        check(saved.contains("# Keep my custom comment"),"User comment lost");
        if(current) {
            check(saved.contains("# Caf\u00e9 \u7761\u7720"),"UTF-8 comment changed");
            Files.writeString(file,saved.replace("sleep.tired.length=600", ""));
            ModConfigs.registerConfigs(); ModConfigs.CONFIG.save();
            saved=Files.readString(file);
            check(entries(saved).get("sleep.tired.length").equals("600"),"Missing default not filled");
            check(entries(saved).get("sleep.day.skip.speed").equals("73.5"),"Filling gap discarded custom value");
            check(entries(saved).get("unknown.future.key").equals("keep=everything"),"Filling gap discarded unknown key");
        }
        try(var files=Files.list(file.getParent())) { check(files.filter(p->p.toString().endsWith(".properties")).count()==1,"Filename changed"); }
        System.out.println("PASS "+args[0]+" "+file.getFileName()+": defaults, custom values, unknown keys, comments, save/reload"+(current?", UTF-8, missing-key merge":""));
    }
}'''


def main():
    java_home = os.environ.get('CONFIG_TEST_JAVA_HOME')
    javac = str(Path(java_home) / 'bin/javac.exe') if java_home else shutil.which('javac')
    java = str(Path(java_home) / 'bin/java.exe') if java_home else shutil.which('java')
    work = ROOT / 'build/config-tests' / uuid.uuid4().hex
    for mode in ('baseline', 'current'):
        source = work / mode / 'src'
        sources = dict(STUBS)
        for name in ('SimpleConfig', 'ModConfigs', 'ModConfigProvider'):
            path = f'{PACKAGE}/{name}.java'
            sources[f'net/vg/sleepcycle/config/{name}.java'] = (
                subprocess.check_output(['git', 'show', 'HEAD:' + path]).decode('utf-8')
                if mode == 'baseline' else (ROOT / path).read_text(encoding='utf-8'))
        sources['ConfigRoundTrip.java'] = HARNESS
        for name, text in sources.items():
            file = source / name
            file.parent.mkdir(parents=True, exist_ok=True)
            file.write_text(text, encoding='utf-8')
        classes = work / mode / 'classes'
        subprocess.run([javac, '-encoding', 'UTF-8', '--release', '17', '-d', str(classes),
                        *map(str, source.rglob('*.java'))], check=True)
        for version in ('1.0.4', '1.0.5'):
            folder = work / mode / version
            result = subprocess.run([java, '-Dfile.encoding=UTF-8', '-Dconfig.folder=' + str(folder),
                                     '-Dconfig.version=' + version, '-cp', str(classes), 'ConfigRoundTrip', mode],
                                    text=True, capture_output=True, check=True)
            print('\n'.join(l for l in result.stdout.splitlines() if l.startswith('PASS ')))
    for version in ('1.0.4', '1.0.5'):
        before = (work / 'baseline' / version / 'defaults.txt').read_text(encoding='utf-8')
        after = (work / 'current' / version / 'defaults.txt').read_text(encoding='utf-8')
        assert before == after, 'Provider default values/comments changed'
    print('CONFIG COMPATIBILITY PASS: historical/current filenames; original defaults/comments unchanged')


if __name__ == '__main__':
    main()
