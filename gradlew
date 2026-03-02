#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Set local scope for the variables with windows NT shell
if [ -n "$CYGWIN" ] || [ -n "$MINGW" ] || [ -n "$MSYS" ]; then
  CYGWIN="true"
fi

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
SAVED="$PWD"
cd "`dirname "$PRG"`/.." > /dev/null
APP_HOME="$PWD"
cd "$SAVED" > /dev/null

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=""

# Use the maximum available, or set MAX_FD != -1 to use that value.
MAX_FD="maximum"

warn () {
    echo "$*"
}

die () {
    echo
    echo "ERROR: $*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN* )
    cygwin=true
    ;;
  Darwin* )
    darwin=true
    ;;
  MINGW* )
    mingw=true
    ;;
esac

# For Cygwin, ensure paths are in UNIX format before anything is touched.
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --unix "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --unix "$CLASSPATH"`
  APP_HOME=`cygpath --unix "$APP_HOME"`
  CYGWIN="$CYGWIN"
fi

# For Darwin, add options to specify how the application appears in the dock
if $darwin; then
  GRADLE_OPTS="$GRADLE_OPTS "-Xdock:name=Gradle" "-Xdock:icon=$APP_HOME/media/gradle.icns""
fi

# For Cygwin, switch paths to Windows format before running java
if $cygwin; then
  [ -n "$JAVA_HOME" ] && JAVA_HOME=`cygpath --path --windows "$JAVA_HOME"`
  [ -n "$CLASSPATH" ] && CLASSPATH=`cygpath --path --windows "$CLASSPATH"`
  [ -n "$APP_HOME" ] && APP_HOME=`cygpath --path --windows "$APP_HOME"`
  CYGWIN="$CYGWIN"
fi

# If a specific java binary isn't specified search for the standard java binaries in the PATH.
if [ -z "$JAVA_HOME" ] ; then
  if $darwin; then
    # OSX
    if [ -x "/usr/libexec/java_home" ] ; then
      export JAVA_HOME=`/usr/libexec/java_home`
    elif [ -d "/Library/Java/JavaVirtualMachines" ]; then
      export JAVA_HOME=`ls -1d /Library/Java/JavaVirtualMachines/*.jdk/Contents/Home | head -n 1`
    fi
  else
    JAVA_PATH=`which java 2>/dev/null`
    if [ "x$JAVA_PATH" != "x" ]; then
      JAVA_HOME=`dirname "$JAVA_PATH"`/..
    fi
  fi
fi

if [ -z "$JAVA_HOME" ]; then
  warn "JAVA_HOME environment variable is not set."
  warn "Java may not be installed."
  die "Unable to find Java. Please set JAVA_HOME or ensure java is in PATH."
fi

JAVA_EXE="$JAVA_HOME/bin/java"
if [ ! -x "$JAVA_EXE" ]; then
  die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME"
  die "Please set the JAVA_HOME variable in your environment to match the location of your Java installation."
fi

# Set the JVM options
if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="$DEFAULT_JVM_OPTS"
fi

# Add GRADLE_OPTS to JAVA_OPTS
if [ -n "$GRADLE_OPTS" ]; then
  JAVA_OPTS="$JAVA_OPTS $GRADLE_OPTS"
fi

# Add application jar to classpath
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Execute Gradle
"$JAVA_EXE" $JAVA_OPTS "-Dorg.gradle.appname=gradle" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
