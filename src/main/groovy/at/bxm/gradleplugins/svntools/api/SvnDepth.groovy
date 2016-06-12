package at.bxm.gradleplugins.svntools.api

/** Checkout/update depth constants, see http://svnbook.red-bean.com/en/1.7/svn.advanced.sparsedirs.html */
enum SvnDepth {

  /** Include only the immediate target of the operation, not any of its file or directory children. */
  EMPTY,
  /** Include the immediate target of the operation and any of its immediate file children. */
  FILES,
  /** Include the immediate target of the operation and any of its immediate file or directory children. The directory children will themselves be empty. */
  IMMEDIATES,
  /** Include the immediate target, its file and directory children, its children's children, and so on to full recursion. */
  INFINITY

  static SvnDepth parse(value) {
    return value ? valueOf(value.toString().toUpperCase()) : null
  }
}
