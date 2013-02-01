package in.ccl.imageloader;

import java.io.File;

import android.content.Context;

public class FileCache {
    
    private File cacheDir;

    /**
     * creates directory to save cached images
     * @param context application context
     */
    public FileCache(Context context){        
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"ccl");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    /**
     * 
     * @param url as {@link String}
     * @return {@link File}
     */
    public File getFile(String url){      
        String filename=String.valueOf(url.hashCode());      
        File f = new File(cacheDir, filename);
        return f;        
    }
    
    /**
     * delete the files, for memory reuse.
     */
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}