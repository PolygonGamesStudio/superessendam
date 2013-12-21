package resource;

import java.io.File;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: rudi
 * Date: 21.12.13
 * Time: 4:15
 * To change this template use File | Settings | File Templates.
 */

public class Iterator implements java.util.Iterator<String> {

    private File        currentFile;
    private boolean     fileFound;
    private Stack<File> prevDirectories = new Stack<File>();


    public Iterator(String path) {
        this.currentFile = null;
        fileFound        = false;
        File rootFile = new File(path);
        if (rootFile.isFile()) {
            currentFile = rootFile;
        }
        else if (rootFile.isDirectory()) {
            if (!dirIsEmpty(rootFile.getPath())) {
                //prevDirectories.push(rootFile);
                moveToBottomLevel(path);
            }
        }
    }

    public String getFileName()
    {
        if(currentFile != null)
        {
            return currentFile.getName();
        }
        else
            return null;
    }
    public String getFile() {
        if (currentFile != null) {
            String currFile = currentFile.getPath();
            moveToNextFile();
            return currFile;
        }
        return null;
    }


    private void moveToNextFile() {
        if (currentFile == null) {
            return;
        }
        getNextFileFromDir(currentFile.getParent(), currentFile);
    }


    private Boolean dirIsEmpty(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            String[] dirList = file.list();
            if (dirList.length == 0) {
                return true;
            }
            return false;
        }
        return null;
    }

    private void getNextFileFromDir(String currentDirPath, File prevFile) {

        File file, currentDir = new File(currentDirPath);
        fileFound = false;

        String[] fileList = currentDir.list();
        for (int i = 0; i < fileList.length; i++) {
            if (fileFound)
                return;
            if ((currentDir + File.separator + fileList[i]).equals(prevFile.getPath())) {
                if (i < fileList.length - 1) {
                    file = new File(currentDirPath + File.separator + fileList[i + 1]);
                    if (file.isFile()) {
                        currentFile = file;
                        fileFound = true;
                    }
                    else if (file.isDirectory() && !dirIsEmpty(file.getPath())) {
                        prevDirectories.push(file);
                        moveToBottomLevel(file.getPath());
                    }
                }
            }
        }
        moveToTopLevel(new File(currentDirPath).getParent());
    }

    private void moveToTopLevel(String parentDir) {
        if (parentDir == null) {
            if (!fileFound)
                currentFile = null;
            return;
        }
        if (fileFound) {
            return;
        }
        if (prevDirectories.isEmpty())    {
            currentFile = null;
            return;
        }
        File prevDirectory = prevDirectories.pop();
        getNextFileFromDir(parentDir, prevDirectory);
    }


    private void moveToBottomLevel(String directory) {
        String[] filesList = new File(directory).list();
        File file;
        for (int i = 0; i < filesList.length; i++) {
            if (fileFound)
                return;
            file = new File(directory + File.separator + filesList[i]);
            if (file.isFile()) {
                currentFile = file;
                fileFound = true;
                return;
            }
            else if (file.isDirectory() && !dirIsEmpty(file.getPath())) {
                prevDirectories.push(file);
                moveToBottomLevel(file.getPath());
            }
        }
    }


    @Override
    public boolean hasNext() {
        return currentFile == null?false:true;
    }

    @Override
    public String next() {
        return getFile();
    }

    @Override
    public void remove() {
        // This method don't support
    }
}