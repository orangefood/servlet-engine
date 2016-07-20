@echo off
REM
REM This batch file installs the Orangefood.com Servlet Engine as a Windows NT Service
REM The Windows Service support is via JavaService a program by Alexandria Software Consulting
REM and can be download from their website at http://www.alexandriasc.com/software/JavaService/index.html
REM
REM The Orangefood.com Servlet Engine must be installed and the JavaService program downloaded and unpacked
REM before this script is run.
REM
REM Instalation process:
REM 1. Copy the JavaService.exe program to the Servlet Engine instalation directory as servletengine-service
REM 2. Run servletengine-service -install ...
REM
REM Uninstalation process:
REM 1. Run servletengine-service -uninstall ...
REM
REM To print out debugging statements search for "REM echo DEBUG^>^>" and replace with "echo DEBUG^>^>"
REM To hide debugging statements search for "echo DEBUG ^>^>" and replace with "REM echo DEBUG^>^>"
REM


setlocal
if "%1"=="-install" set MODE=install
if "%1"=="-uninstall" set MODE=uninstall 
REM echo DEBUG^>^> MODE=%MODE%
if "%MODE%"=="" goto usage
set SERVICE_NAME=Orangefood.com Servlet Engine
REM echo DEBUG^>^> SERVICE_NAME=%SERVICE_NAME%
REM Parse the parameters
goto parsestart
goto end

:uninstall
echo Uninstalling...
if "%SERVER_DIR%"=="" set SERVER_DIR=C:\Program Files\orangefood-servletengine
REM echo DEBUG^>^> SERVER_DIR=%SERVER_DIR%
if not exist "%SERVER_DIR%\servletengine-service.exe" goto uninstall_notfound_err
REM echo DEBUG^>^> SERVER_DIR ok
echo About to uninstall Orangefood.com Servlet Engine as a service with these settings:
echo Servlet Engine installed at: %SERVER_DIR%
echo JavaService program located at: %SERVER_DIR%\servletengine-service.exe
pause
"%SERVER_DIR%\servletengine-service" -uninstall "%SERVICE_NAME%"
if not errorlevel 0 goto uninstall_err
echo.
echo Uninstall complete
goto end

:install
REM echo DEBUG^>^> Installing...

REM set default values
REM echo DEBUG^>^> Checking defaults
if "%SERVER_DIR%"=="" set SERVER_DIR=C:\Program Files\orangefood-servletengine
REM echo DEBUG^>^> SERVER_DIR=%SERVER_DIR%
if not exist "%SERVER_DIR%\orangefood-servletengine.jar" goto server_dir_err
REM echo DEBUG^>^> SERVER_DIR ok
 
if "%JAVASERVICE%"=="" set JAVASERVICE=C:\Program Files\javaservice
REM echo DEBUG^>^> JAVASERVICE=%JAVASERVICE%
if not exist "%JAVASERVICE%\bin\JavaService.exe" goto javaservice_err
REM echo DEBUG^>^> JAVASERVICE ok

if "%PASSWORD%"=="" set PASSWORD=shutdownpass
REM echo DEBUG^>^> PASSWORD=%PASSWORD%

if "%PORT%"=="" set PORT=9009
REM echo DEBUG^>^> PORT=%PORT%

if "%JAVA_HOME%"=="" goto java_home_err
if not exist "%JAVA_HOME%\jre\bin\server\jvm.dll" goto java_home_dll_err
REM echo DEBUG^>^> JAVA_HOME=%JAVA_HOME%

echo About to install Orangefood.com Servlet Engine as a service with these settings:
echo Servlet Engine installed at: %SERVER_DIR%
echo JavaService program located at: %JAVASERVICE%
echo Shutdown password: %PASSWORD%
echo Shutdown port: %PORT%
pause

if exist "%SERVER_DIR%\servletengine-service.exe" goto installservice
echo Copying "%JAVASERVICE%\bin\JavaService.exe" to "%SERVER_DIR%\servletengine-service.exe" ...
copy "%JAVASERVICE%\bin\JavaService.exe" "%SERVER_DIR%\servletengine-service.exe"
if not errorlevel 0 goto copy_err

:installservice
"%SERVER_DIR%\servletengine-service" -install "%SERVICE_NAME%" "%JAVA_HOME%\jre\bin\server\jvm.dll" "-Djava.class.path=%SERVER_DIR%\orangefood-servletengine.jar" -start com.orangefood.se.Server -stop com.orangefood.se.StopServer -params "%PASSWORD%" %PORT% -out "%SERVER_DIR%\out.log" -err "%SERVER_DIR%\err.log" -current "%SERVER_DIR%"

if not errorlevel 0 goto install_err
echo.
echo Install complete.  Installed as %SERVICE_NAME%. Go to the Control Panel and open Service to start the service.
goto end

REM 
REM Option parsing
REM
:parsestart
shift
if "%~1"=="" goto parsedone
if not "%OPTION%"=="" goto setvalue 
REM echo DEBUG^>^> set OPTION=%1
set OPTION=%1
goto parsestart

:setvalue
REM echo DEBUG^>^> set %OPTION%=%~1
if %OPTION%==-server_dir set SERVER_DIR=%~1
if %OPTION%==-javaservice set JAVASERVICE=%~1
if %OPTION%==-password set PASSWORD=%~1
if %OPTION%==-port set PORT=%1
set OPTION=
goto parsestart

:parsedone
goto %MODE%


REM
REM Error labels
REM

:server_dir_err
echo.
echo Couldn't find Orangefood.com Servlet Engine in %SERVER_DIR%, check settings.
echo.
goto usage

:javaservice_err
echo.
echo Couldn't find JavaService in %JAVASERVICE%, check settings.
echo.
goto usage

:java_home_err
echo.
echo JAVA_HOME not set.  Set JAVA_HOME to the location of the installed JVM.
echo.
goto usage

:java_home_dll_err
echo.
echo File "%JAVA_HOME%\jre\bin\server\jvm.dll" not found.  Check that Java is installed correctly
goto end

:copy_err
echo.
echo Error copying files, check permissions and readonly flags.
goto end

:uninstall_notfound_err
echo.
echo File "%SERVER_DIR%\servletengine-service.exe" not found.  
echo.
goto usage

:install_err
echo.
echo Install failed
echo.
goto end

:uninstall_err
echo.
echo Uninstall failed.
echo.
goto end

:usage
echo Installs or uninstalls NT Service support for the Orangefood.com Servlet Engine
echo.
echo %0 [-install ^| -uninstall] [options...]
echo.
echo Install options:
echo -server_dir [path]		Sets the directory the server is installed in
echo -javaservice [path]	Sets the path to the JavaService program
echo -password [password]	Sets the shutdown password to use
echo -port [port]		Sets the port number for the shutdown listener
echo.
echo Uninstall options:
echo -server_dir [path]		Sets the directory the server is installed in
echo.

:end
