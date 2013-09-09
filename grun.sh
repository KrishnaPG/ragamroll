CURDIR=$(dirname ${0})
JF_JAR=${CURDIR}/ext-libs/jfugue-4.1.0-2011-02-03.jar

CMD="groovy -cp ${JF_JAR}:${CURDIR} $@"
$CMD
