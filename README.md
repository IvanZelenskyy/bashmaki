# bashmaki
Расчет башмаков по правилам Укрзализныци

# Как пользоваться:

1. Скачать с помощью git или тут, на страничке выкачать зип.
2. Отредактировать файл build.gradle, заменив значения переменных _os, window_toolkit, proc_. Возможные значения:
 - для os: macosx, linux, windows
 - для proc: x86_64, amd64, x86
 - для window_toolkit: cocoa, win32, gtk

## способ первый:
3. Запустить _./gradlew clean distZip_
4. Файл _buid/distributions/bashmaki_*.jar_ скопировать туда, где он будет использоваться, и там его распаковать.
5. После распаковки зайти в папку _bashmaki_ и выполнить _bin/bashmaki_ или _bin/bashmaki.bat_

## способ второй:
3. Запустить _./gradlew clean installDist_
4. Зайти в папку _build/install/bashmaki_ и выполнить _bin/bashmaki_ или _bin/bashmaki.bat_