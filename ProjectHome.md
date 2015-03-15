# What it is #

**Hamcrest-Reflection** provides a simple API for performing reflection with [Hamcrest](http://hamcrest.org/JavaHamcrest/) matchers.

# How to get it #
**Hamcrest-Reflection** can be obtained in two ways: Either by manual download, or by automatic dependency resolution using tools like [Maven](http://maven.apache.org/), Ivy or Grails.

## Manual Download ##
JARs can be downloaded from [Maven Central](http://search.maven.org/#browse%7C1677369981).

## Automatic Dependency Resolution ##
```
<dependency>
  <groupId>com.natpryce.hamcrest</groupId>
  <artifactId>hamcrest-reflection</artifactId>
  <version>[0.1, 1.0)</version>
</dependency>
```

# How to use it #

**Hamcrest-Reflection** provides a set of Hamcrest matchers making it pretty easy to check for declarative correctness. It answers questions like "Is this class annotated in an expected way?" or "Is this method static?" etc.

Simply try out the `HasAnnotationMatcher` and `ModifierMatcher` etc., these are mostly self-explaining! :-)

# How to contribute #

**Hamcrest-Reflection** is an community effort, hence we love to accept your contributions!

## Report bugs and request features ##

Please use the [issue tracker](https://code.google.com/p/hamcrest-reflection/issues) to let us know of any bug or wanted feature.

## Fix bugs and develop features ##

If you fixed a bug or have developed a new feature, please add a patch to the particular issue in the tracker. We will try to merge it, and get back to you if there are any questions with that.

### Get code and build it ###

**Hamcrest-Reflection** uses [Subversion](http://subversion.apache.org/) and [Maven](http://maven.apache.org/), so it is pretty simple to obtain the source and build from scratch once these tools are installed properly:

```
mvn scm:bootstrap -Dproject.basedir=. -Dgoals=test -DconnectionUrl=scm:svn:http://hamcrest-reflection.googlecode.com/svn/trunk/
```

This downloads from SVN, compiles and tests in one single step. After that, you can import the project into your favorite IDE, and / or use `mvn test` to compile and test it.

# Credits #

This code was originally authored by [Nat PRYCE](http://www.natpryce.com). Currently [Markus KARG](http://www.headcrashing.eu) is managing the code. Certainly there where more people involved, which you can find in the [list of contributors](https://code.google.com/p/hamcrest-reflection/people/list).