# Copyright (c) 2010, Mani Balasubramanian <ragamroll@gmail.com>
# All rights reserved.

# Modified from one of the makefiles included in SMC

SM_SOURCES=     TokenizerClass.sm
SOURCES=        $(SM_SOURCES:%.sm=%Context.groovy)
TOKENIZERTEST=    $(GROOVY) $(GROOVY_FLAGS) TokenizerTest

# Uncomment to turn on debug message generation.
#TRACE=          -g

SMC_JAR=        Smc.jar
SMC=            java -jar $(SMC_JAR)
SMC_FLAGS=      -groovy $(TRACE) $(SERIAL) $(SYNC) $(REFLECT)

GROOVY=         groovy
STATEMAP_JAR =  statemap.jar
GROOVY_FLAGS=   -classpath $(STATEMAP_JAR)

RM_F=           rm -f

# Rules

%Context.groovy : %.sm
		$(SMC) $(SMC_FLAGS) $<

all :           TokenizerTest

TokenizerTest :   $(SOURCES)

test :          $(OBJECTS)
		-$(TOKENIZERTEST) "$$(cat vathapi.srgm)"

clean :
		-$(RM_F) *Context.groovy
		-$(RM_F) *_sm.dot
		-$(RM_F) *_sm.png
		-$(RM_F) *_sm.html
