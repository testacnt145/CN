package com.chattynotes.lazylistPath;

import java.io.File;

class FileCachePath {
    
    private File file;

    File getFile(String path){
    	file = new File(path);
		return file;
    }
    
    public void clear(){
    	//LogUtil.e("--->>> fileCache.clear()...", file.listFiles() + "...");
        File[] files = file.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}