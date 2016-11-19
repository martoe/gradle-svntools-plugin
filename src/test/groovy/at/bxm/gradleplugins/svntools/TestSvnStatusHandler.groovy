package at.bxm.gradleplugins.svntools

import groovy.util.logging.Log
import org.tmatesoft.svn.core.SVNException
import org.tmatesoft.svn.core.wc.ISVNStatusHandler
import org.tmatesoft.svn.core.wc.SVNStatus

import static org.tmatesoft.svn.core.SVNNodeKind.*
import static org.tmatesoft.svn.core.wc.SVNStatusType.*

@Log
class TestSvnStatusHandler implements ISVNStatusHandler {

  final added = new TreeSet()
  final deleted = new TreeSet()
  final unversioned = new TreeSet()

  def getAdded() {
    return added as List
  }

  def getDeleted() {
    return deleted as List
  }
  def getUnversioned() {
    return unversioned as List
  }
  
  @Override
  void handleStatus(SVNStatus status) throws SVNException {
    switch (status.kind) {
      case DIR:
        switch (status.contentsStatus) {
          case STATUS_NORMAL:
            switch (status.nodeStatus) {
              case STATUS_ADDED:
                added << status.file
                return
              case STATUS_NORMAL: // unchanged
                return
            }
        }
      case FILE:
        switch (status.contentsStatus) {
          case STATUS_MODIFIED:
            switch (status.nodeStatus) {
              case STATUS_ADDED:
                added << status.file
                return
            }
          case STATUS_NORMAL:
            switch (status.nodeStatus) {
              case STATUS_DELETED:
                deleted << status.file
                return
              case STATUS_NORMAL: // unchanged
                return
            }
        }
      case UNKNOWN:
        switch (status.contentsStatus) {
          case STATUS_NONE:
            switch (status.nodeStatus) {
              case STATUS_UNVERSIONED:
                unversioned << status.file
                return
            }
        }
    }
    log.warning("unknown $status.file.absolutePath: kind=$status.kind, contents=$status.contentsStatus, node=$status.nodeStatus")
  }
}
