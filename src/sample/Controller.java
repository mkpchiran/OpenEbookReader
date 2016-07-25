package sample;

import java.io.*;

import com.google.common.io.Files;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Controller {

    public Path unzip(InputStream is)  {

        File folder = Files.createTempDir();
        String destination = folder.toString();



        try (ZipInputStream zis = new ZipInputStream(is)) {
            ZipEntry entry = zis.getNextEntry();
            while (entry != null) {

                String fileName = entry.getName();
                File newFile = new File(destination + File.separator + fileName);

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder
                new File(newFile.getParent()).mkdirs();

                if (!entry.isDirectory()) {

                    FileOutputStream fos = new FileOutputStream(newFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);

                    int len;
                    byte chunk[] = new byte[1024];

                    while ((len = zis.read(chunk, 0, 1024)) > 0) {
                        bos.write(chunk, 0, len);
                    }

                    bos.close();
                }
                entry = zis.getNextEntry();
            }

            zis.closeEntry();

        }
        catch (IOException e) {
           e.printStackTrace();
        }

        return Paths.get(destination);
    }

    public Path getOpf(Path Dir){
        File opf=null;
        try {
            Path tt = Dir;




            System.out.println(tt);

            File file;
            try {

                File[] fileArr = new File(tt.toFile().getPath()).listFiles();
                for ( File opfTemp : fileArr) {
                    File[] tmpAA=null;
                    if(opfTemp.isDirectory()){
                     tmpAA=opfTemp.listFiles();

                    }else{
                    	if (opfTemp.getAbsolutePath().endsWith(".opf")) {
                    		opf=opfTemp;
                    		break;
                    	}else
                        continue;
                    }
                    for (File tmp : tmpAA) {

                    System.out.println(tmp.getPath());
                    if (tmp.getAbsolutePath().endsWith(".opf")) {
                        file = tmp;
                        opf=tmp;


                        break;
                    }

                }


                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return opf.toPath();

    }

    public List<String> listFiles(File file){
List<String> list=new LinkedList<String>();
        org.jsoup.nodes.Document doc = null;
        try {
            doc = Jsoup.parse(file, null);
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
        Elements newsHeadlines = doc.getElementsByTag("item");

        for(int a=0;a<newsHeadlines.size();a++){
            if(newsHeadlines.get(a).attr("href").endsWith(".xhtml"))
                                            list.add(newsHeadlines.get(a).attr("href"));
//                                                vegName.addElement(newsHeadlines.get(a).attr("href"));
//                                            browser.loadURL("ftp://"+newsHeadlines.get(a).attr("href"));
        }


        return list;
    }

    public static void main(String...aaa){

        new Controller().listFiles(new File("/home/chiranz/OPS/package.opf"));


    }

}
