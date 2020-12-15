@echo off

set relVer=%1

if not exist "%USERPROFILE%/keystore" (
  echo [91mCan't find the "keystore" file in the home folder ^("%USERPROFILE%"^)[m
  goto :EOF
)
if "%storepass%" == "" (
  echo [91mSet storepass to your keystore password[m
  goto :EOF
)

if "%relVer%" == "" (
  echo [91mYou need to provide the version to sign[m
  goto :EOF
)
if not exist "features\net.mihai-nita.ansicon_%relVer%.jar" (
  echo [91mThere is no file named "features\net.mihai-nita.ansicon_%relVer%.jar"[m
  goto :EOF
)
if not exist "plugins\net.mihai-nita.ansicon.plugin_%relVer%.jar" (
  echo [91mThere is no file named "plugins\net.mihai-nita.ansicon.plugin_%relVer%.jar"[m
  goto :EOF
)

set tsa=http://timestamp.comodoca.com/rfc3161
set alias=Personal Projects

call:jar2Xz artifacts
call:jar2Xz content

call:jarSign artifacts.jar
call:jarSign content.jar
call:jarSign features\net.mihai-nita.ansicon_%relVer%.jar
call:jarSign plugins\net.mihai-nita.ansicon.plugin_%relVer%.jar

echo [32mALL DONE[m

goto :EOF

:jarSign
  echo [93mSign - "%1"[m
  jarsigner -strict -tsa %tsa% -storepass:env storepass "%1" "%alias%"
  echo [93mVerify sign - "%1"[m
  jarsigner -verify -strict "%1"
goto:EOF

:jar2Xz
  echo [93mZx - "%1"[m
  del "%1.xml.xz"
  7za e -bsp0 -bso0 -aoa "%1.jar" "%1.xml"
  7za a -bsp0 -bso0 -sdel -mx9 "%1.xml.xz" "%1.xml"
goto:EOF
