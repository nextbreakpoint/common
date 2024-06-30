.PHONY: clean
clean:
	mvn clean

.PHONY: compile
compile:
	mvn compile

.PHONY: package
package:
	mvn package -DskipTests=true

.PHONY: install
install:
	mvn install -DskipTests=true

.PHONY: verify
verify:
	mvn verify

.PHONY: dist
dist:
	rm -fR dist
	mkdir -p dist
	cp target/com.nextbreakpoint.common-*.jar dist

.PHONY: github-release
github-release:
	mvn -Dchannel=github clean package de.jutzig:github-release-plugin:release -Dgithub.draft=true

.PHONY: maven-release
maven-release:
	mvn -Dchannel=ossrh clean deploy

.PHONY: set-version
set-version:
	test $(version)
	mvn versions:set -DnewVersion=$(version)
	mvn versions:commit
