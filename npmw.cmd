@REM
@REM Money Market Bi - BI Microservice for Money Market Bi deals is part of the Granular Bi System
@REM Copyright © 2025 Edwin Njeru (mailnjeru@gmail.com)
@REM
@REM This program is free software: you can redistribute it and/or modify
@REM it under the terms of the GNU General Public License as published by
@REM the Free Software Foundation, either version 3 of the License, or
@REM (at your option) any later version.
@REM
@REM This program is distributed in the hope that it will be useful,
@REM but WITHOUT ANY WARRANTY; without even the implied warranty of
@REM MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
@REM GNU General Public License for more details.
@REM
@REM You should have received a copy of the GNU General Public License
@REM along with this program. If not, see <http://www.gnu.org/licenses/>.
@REM

@echo off

setlocal

set NPMW_DIR=%~dp0

set NODE_EXE=^"^"
set NODE_PATH=%NPMW_DIR%target\node\
set NPM_EXE=^"%NPMW_DIR%target\node\npm.cmd^"
set INSTALL_NPM_COMMAND=^"%NPMW_DIR%mvnw.cmd^" -Pwebapp frontend:install-node-and-npm@install-node-and-npm

if not exist %NPM_EXE% (
  call %INSTALL_NPM_COMMAND%
)

if exist %NODE_EXE% (
  Rem execute local npm with local node, whilst adding local node location to the PATH for this CMD session
  endlocal & echo "%PATH%"|find /i "%NODE_PATH%;">nul || set "PATH=%NODE_PATH%;%PATH%" & call %NODE_EXE% %NPM_EXE% %*
) else if exist %NPM_EXE% (
  Rem execute local npm, whilst adding local npm location to the PATH for this CMD session
  endlocal & echo "%PATH%"|find /i "%NODE_PATH%;">nul || set "PATH=%NODE_PATH%;%PATH%" & call %NPM_EXE% %*
) else (
  call npm %*
)
