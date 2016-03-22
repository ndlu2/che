package org.eclipse.che.jdt.search;

import org.eclipse.che.ide.ext.java.BaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * //todo
 *
 * @author Alexander Andrienko
 */
public class FindFilesByFqnTest extends BaseTest {

    private final DifficultMultiModuleProjectSetup difficultMultiModuleProjectSetup;

    public FindFilesByFqnTest() {
        difficultMultiModuleProjectSetup = new DifficultMultiModuleProjectSetup();
    }

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        difficultMultiModuleProjectSetup.setUp();
    }

    @After
    public void tearDown() throws Exception {
        difficultMultiModuleProjectSetup.tearDown();
    }

    @Test
    public void testFindElementsByFqn() throws Exception {
//        IJavaProject pr = DifficultMultiModuleProjectSetup.getProject();
//        IPackageFragmentRoot root = ((JavaProject)pr).getPackageFragmentRoot(new Path(DifficultMultiModuleProjectSetup.ROOT_SRC_CONTAINER));
//        IPackageFragment packageFragment0 = root.createPackageFragment("che", true, null);
//        StringBuilder a0 = new StringBuilder();
//        a0.append("package che;\n");
//        a0.append("public class A {}\n");
//        packageFragment0.createCompilationUnit("A.java", a0.toString(), true, null);
//
//        IPackageFragment packageFragment = root.createPackageFragment("che.example.com", true, null);
//        StringBuilder a = new StringBuilder();
//        a.append("package che.example.com;\n");
//        a.append("public class Antey {");
//        a.append(" \nprivate static class Nested{\\n}");
//        a.append("\n}\n");
//        packageFragment.createCompilationUnit("Antey.java", a.toString(), true, null);
//        StringBuilder b = new StringBuilder();
//        b.append("package che.example.com;\n");
//        b.append("import java.util.Comparator;\n");
//        b.append("public class B extends Antey implements Comparator<Antey>{\n");
//        b.append("   @Override\n");
//        b.append("   public int compare(Antey o1, Antey o2) \n{\n");
//        b.append("       Antey bb = null;\n");
//        b.append("       return 0;\n");
//        b.append("   }\n");
//        b.append("}\n");
//        packageFragment.createCompilationUnit("B.java", b.toString(), true, null);
//
//        JavaModelManager.getIndexManager().indexAll(pr.getProject());//test doesn't work without indexation
//
//        char[][] packages = {{'c', 'h', 'e', '.', 'e', 'x', 'a', 'm', 'p', 'l', 'e', '.', 'c', 'o', 'm'}};
//        char[][] names = {{'A', 'n', 't', 'e', 'y'}};
//
//        List<IJavaElement> result = SearchTestHelper.runTypeRefByFQN(packages, names);
//
//        assertEquals(result.size(), 1);
//        assertEquals("Antey", result.get(0).getElementName());
    }

//    @Test
//    public void findElementsByFanInTheMultiModuleProject() throws JavaModelException {
//        IJavaProject javaProject = DifficultMultiModuleProjectSetup.getProject();
//        JavaModelManager.getIndexManager().indexAll(javaProject.getProject());
//
//        char[][] packages = {{'o', 'r', 'g', '.', 'e', 'c', 'l', 'i', 'p', 's', 'e', '.', 'c', 'h', 'e'}};
//        char[][] names = {{'S', 'i', 'm', 'p', 'l', 'e', 'A', 'd', 'd', 'r', 'e', 's', 's'}};
//
//        List<IJavaElement> result = SearchTestHelper.runTypeRefByFQN(packages, names);
//
//        assertEquals(result.size(), 1);
//        assertEquals("Antey", result.get(0).getElementName());
//    }
//
//    @Test
//    public void findElementsByFanInTheMultiModuleProject2() throws JavaModelException {
//        IJavaProject javaProject = DifficultMultiModuleProjectSetup.getProject();
//        JavaModelManager.getIndexManager().indexAll(javaProject.getProject());
//
//        char[][] packages = {{'o', 'r', 'g', '.', 'e', 'c', 'l', 'i', 'p', 's', 'e', '.', 'c', 'h', 'e'}};
//        char[][] names = {{'A', 'p', 'p'}};
//
//        List<IJavaElement> result = SearchTestHelper.runTypeRefByFQN(packages, names);
//
//        assertEquals(result.size(), 1);
//        assertEquals("App", result.get(0).getElementName());//tod mistake
//    }
}
