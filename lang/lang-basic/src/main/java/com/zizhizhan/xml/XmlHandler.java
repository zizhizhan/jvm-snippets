package com.zizhizhan.xml;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import java.io.*;

public class XmlHandler {

    private DocumentBuilder builder;
    private Document doc;
    private Transformer trans;
    private DOMSource ds;
    private String docFile;

    public static void main(String[] args) {
        XmlHandler xh = XmlHandler.newInstance("ccc.xml");
        System.out.println("************************************Begin************************************");
        try{
            System.out.println("Begin");
            xh.loadTemplate("ServerTemplate.xml");
            System.out.println("************************************End************************************");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    protected XmlHandler(String uri){
        try {

            docFile = uri;
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            trans = TransformerFactory.newInstance().newTransformer();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static XmlHandler newInstance(String uri){
        return new XmlHandler(uri);
    }


    public String getFile(){
        return docFile;
    }

    public boolean copy(String dest, String src){
        File serverXml = new File(dest);
        try {
            if(!serverXml.exists()){
                serverXml.createNewFile();
                byte[] buf = new byte[1024*10];
                FileInputStream fis = new FileInputStream(src);
                int len = fis.read(buf);
                FileOutputStream fos = new FileOutputStream(serverXml);
                fos.write(buf, 0, len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        printDocument("copy");
        return true;
    }

    public void loadTemplate(String uri) throws FileNotFoundException, TransformerException, Exception {

        StreamResult sr = new StreamResult(new FileOutputStream(docFile));
        DOMSource doms = new DOMSource(builder.parse(uri));
        trans.transform(doms, sr);

    }

    public void load() throws Exception{
        try {

            doc = builder.parse(docFile);
            ds = new DOMSource(doc);

        } catch (Exception ex) {
            throw new Exception("加载文件失败!");
        }
    }

    public void load(String uri) throws Exception{

        docFile = uri;
        load();
    }

    public void setItem(String key, String value) throws Exception{

        if(doc == null){
            throw new Exception("当前文档不存在");
        }

        Element item = (Element)doc.getElementsByTagName(key).item(0);

        if(item == null){
            throw new Exception("文档不存在元素: " + key + ", 请检查程序");
        }
        item.setTextContent(value);
    }

    public void save() throws FileNotFoundException, TransformerException {

        StreamResult sr = new StreamResult(new FileOutputStream(docFile));
        trans.transform(ds, sr);

    }

    public void printDocument(String area){
        try{
            System.out.println("****************************" + area + "***************************");
            BufferedReader br = new BufferedReader(new FileReader("1.xml"));
            while(br.ready()){
                System.out.println(br.readLine());
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
