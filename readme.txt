Setup:

Needs integration library for Cayenne:

https://github.com/NDSU-Information-Technology/tapestry5-cayenne

You need to get the f99b version at the moment from master.

You also need the snapshot version of the tynamo-federated accounts project:

https://github.com/tynamo/tynamo-federatedaccounts

Both cases install with mvn install -DskipTests

If you have access to ECI's repo, these should be taken care of for you.

At the moment the only federated accounts that work is Facebook. You will need to configure Facebook oauth to login. Just 
search for their developer site to register a new OAuth login. The default login for tynamo-federated accounts asks for
way too many attributes. That will need to be fixed.

You will need to use Apache Cayenne 3.0.2 modeler to work on project.

Copy jetty-env-template.xml to jetty-env.xml and apply your own settings. jetty-env.xml is in svn ignores, as it shouldn't
be committed, so leave it in the ignore.

You will also need to include in your runtime classpath the MySQL driver, Commons DBCP, and Commons Pool. We don't include
DB dependencies in our projects, and the two Commons projects are only needed with the current Jetty 6 setup.