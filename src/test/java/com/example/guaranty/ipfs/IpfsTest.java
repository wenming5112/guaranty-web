package com.example.guaranty.ipfs;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author ming
 * @version 1.0.0
 * @date 2020/12/3 18:08
 **/
public class IpfsTest {
    private IPFS ipfs;

    //<repositories>
    //    <repository>
    //        <id>jitpack.io</id>
    //        <url>https://jitpack.io</url>
    //    </repository>
    //  </repositories>
    //
    //  <dependencies>
    //    <dependency>
    //      <groupId>com.github.ipfs</groupId>
    //      <artifactId>java-ipfs-http-client</artifactId>
    //      <version>v1.2.2</version>
    //    </dependency>
    //  </dependencies>

    @Before
    public void setUp() {
        ipfs = new IPFS("/ip4/47.108.143.240/tcp/5001");
    }

    @Test
    public void myTest() throws IOException {
        //保存上传文件
        NamedStreamable.FileWrapper saveFile = new NamedStreamable.FileWrapper(new File("E:\\guaranty\\233ade8f1076f5b3f8a4a02def39d1ef.png"));
        MerkleNode result = ipfs.add(saveFile).get(0);
        System.out.println(result.hash);

    }

    @Test
    public void bigFile() throws IOException {
        //保存上传文件
        NamedStreamable.FileWrapper saveFile = new NamedStreamable.FileWrapper(new File("E:\\guaranty\\有神山海1015.apk"));
        MerkleNode result = ipfs.add(saveFile).get(0);
        System.out.println(result.hash);

    }

    @Test
    public void dirTest() throws IOException {
        Path test = Files.createTempDirectory("test");
        Files.write(test.resolve("file.txt"), "G'day IPFS!".getBytes());
        NamedStreamable dir = new NamedStreamable.FileWrapper(test.toFile());
        List<MerkleNode> add = ipfs.add(dir);
        MerkleNode addResult = add.get(add.size() - 1);
        List<MerkleNode> ls = ipfs.ls(addResult.hash);
        Assert.assertTrue(ls.size() > 0);
    }

    @Test
    public void download() throws IOException {
        // 大文件下载读取超时
        //下载文件参数为文件 hash
//        Multihash filePointer = Multihash.fromBase58("QmcbJuTRYyxWerJrMUJsnEX68CCEoGYbEv5gXNQqavptew");
        Multihash filePointer = Multihash.fromBase58("QmcJ1WWjKUKXPFGgx2gMMvnFsoRM3n3ce78dy3YEVyuj6i");
        byte[] fileContents = ipfs.cat(filePointer);

        //保存文件
//        File downloadFile = new File("E:\\guaranty\\test\\233ade8f1076f5b3f8a4a02def39d1ef-test1.png");
        File downloadFile = new File("E:\\guaranty\\test\\有神山海1015-test.apk");

        if (!downloadFile.exists()) {
            if (downloadFile.createNewFile()) {
                System.out.println(String.format("文件保存成功: %s", downloadFile.getAbsolutePath()));
            }
        }

        try (FileOutputStream fop = new FileOutputStream(downloadFile)) {
            fop.write(fileContents);
            fop.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
