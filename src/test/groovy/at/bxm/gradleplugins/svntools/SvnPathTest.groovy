package at.bxm.gradleplugins.svntools

import org.tmatesoft.svn.core.SVNURL
import spock.lang.Specification

class SvnPathTest extends Specification {

  def "parse trunk"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/trunk", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.trunk == true
    path.path == "/"
  }

  def "parse trunk with file"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/trunk/path/to/file.txt", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.trunk == true
    path.path == "/path/to/file.txt"
  }

  def "parse branch"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/branches/mybranch", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.branch == true
    path.branchName == "mybranch"
    path.path == "/"
  }

  def "parse branch with file"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/branches/mybranch/path/to/file.txt", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.branch == true
    path.branchName == "mybranch"
    path.path == "/path/to/file.txt"
  }
  def "parse tag"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/tags/mytag", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.tag == true
    path.tagName == "mytag"
    path.path == "/"
  }

  def "parse tag with file"() {
    when:
    def path = SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/tags/mytag/path/to/file.txt", false))
    then:
    path.moduleBasePath == "/repo/mymodule"
    path.tag == true
    path.tagName == "mytag"
    path.path == "/path/to/file.txt"
  }

  def "invalid path"() {
    when:
    SvnPath.parse(new SVNURL("http://localhost/repo/mymodule/invalid/path", false))
    then:
    thrown MalformedURLException
  }
}
