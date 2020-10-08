default:
	cat Makefile

FOLDER_STATIC       := src/main/resources/static

BOOTSTRAP_VERSION   := 4.5.2
BOOTSTRAP_LINK      := https://github.com/twbs/bootstrap/releases/download/v$(BOOTSTRAP_VERSION)
BOOTSTRAP_EXAMPLES  := bootstrap-4.5.2-examples.zip
BOOTSTRAP_FILE      := bootstrap-$(BOOTSTRAP_VERSION)-dist.zip
BOOTSTRAP_FLDR_SRC  := install/bootstrap/$(BOOTSTRAP_VERSION)
BOOTSTRAP_FLDR_DST  := $(FOLDER_STATIC)/lib/bootstrap/$(BOOTSTRAP_VERSION)
BOOTSTRAP_FILE_SRC  := $(BOOTSTRAP_FLDR_SRC)/$(BOOTSTRAP_VERSION).zip

run:
	./gradlew bootRun

clean:
	./gradlew clean

build: clean
	./gradlew bootRun

#
#
#
bootstrap_download:
	mkdir -p   $(BOOTSTRAP_FLDR_SRC)                                                            && \
	wget -q -N $(BOOTSTRAP_LINK)/$(BOOTSTRAP_FILE)     -O $(BOOTSTRAP_FILE_SRC)                 && \
	wget -q -N $(BOOTSTRAP_LINK)/$(BOOTSTRAP_EXAMPLES) -O $(BOOTSTRAP_FLDR_SRC)/examples.zip


bootstrap_install:
	rm    -rf                            $(BOOTSTRAP_FLDR_DST)                                  && \
	mkdir -p                             $(BOOTSTRAP_FLDR_DST)                                  && \
	unzip -q -j $(BOOTSTRAP_FILE_SRC) -d $(BOOTSTRAP_FLDR_DST)                                  && \
	cd                          $(BOOTSTRAP_FLDR_DST)/..                                        && \
    rm    -f                    current                                                         && \
    ln    -s  -f                $(BOOTSTRAP_VERSION) current
	
#
# https://code.jquery.com/jquery-3.5.1.min.js
# https://code.jquery.com/jquery-3.5.1.min.map
JQUERY_VERSION   := 3.5.1
JQUERY_LINK      := https://code.jquery.com/

JQUERY_FILE      := jquery-$(JQUERY_VERSION).min
JQUERY_FLDR_SRC  := install/jquery/$(JQUERY_VERSION)
JQUERY_FLDR_DST  := $(FOLDER_STATIC)/lib/jquery/$(JQUERY_VERSION)
JQUERY_FILE_SRC  := $(JQUERY_FLDR_SRC)/$(JQUERY_VERSION)

jquery_download:
	mkdir -p   $(JQUERY_FLDR_SRC)                                                               && \
	wget -q -N $(JQUERY_LINK)/$(JQUERY_FILE).js  -O $(JQUERY_FILE_SRC).js                       && \
	wget -q -N $(JQUERY_LINK)/$(JQUERY_FILE).map -O $(JQUERY_FILE_SRC).map


jquery_install:
	rm    -rf                   $(JQUERY_FLDR_DST)                                              && \
	mkdir -p                    $(JQUERY_FLDR_DST)                                              && \
	cp $(JQUERY_FILE_SRC).js    $(JQUERY_FLDR_DST)/jquery.min.js                                && \
	cp $(JQUERY_FILE_SRC).map   $(JQUERY_FLDR_DST)/jquery.min.map                               && \
	cd                          $(JQUERY_FLDR_DST)/..                                           && \
	rm    -f                    current                                                         && \
	ln    -s  -f                $(JQUERY_VERSION) current

#
# https://unpkg.com/@popperjs/core@2
POPPERJS_VERSION   := 2
POPPERJS_LINK      := https://unpkg.com/@popperjs/core@$(POPPERJS_VERSION)

POPPERJS_FILE      := popper-$(POPPERJS_VERSION).js
POPPERJS_FLDR_SRC  := install/popper.js/$(POPPERJS_VERSION)
POPPERJS_FLDR_DST  := $(FOLDER_STATIC)/lib/popper.js/$(POPPERJS_VERSION)
POPPERJS_FILE_SRC  := $(POPPERJS_FLDR_SRC)/$(POPPERJS_VERSION)

popperjs_download:
	mkdir -p   $(POPPERJS_FLDR_SRC)                                                     && \
	wget -q -N $(POPPERJS_LINK) -O $(POPPERJS_FILE_SRC).js

popperjs_install:
	rm    -rf                   $(POPPERJS_FLDR_DST)                                    && \
	mkdir -p                    $(POPPERJS_FLDR_DST)                                    && \
	cp $(POPPERJS_FILE_SRC).js  $(POPPERJS_FLDR_DST)/popper.min.js                      && \
	cd                          $(POPPERJS_FLDR_DST)/..                                 && \
	rm    -f                    current                                                 && \
	ln    -s  -f                $(POPPERJS_VERSION) current
	