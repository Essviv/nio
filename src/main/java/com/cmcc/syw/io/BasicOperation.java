package com.cmcc.syw.io;

import com.cmcc.syw.utils.Student;

import java.io.*;
import java.util.Formatter;
import java.util.Scanner;

/**
 * demo to use stream classes within java
 * <p>
 * Created by sunyiwei on 16/4/27.
 */
public class BasicOperation {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //stream operation
        streamOp();

        //char operation
        charOp();

        //data input/output operation
        dataOp();

        //obj input/output operation
        objOp();

        //scanner
        scanner();

        //formatter
        formatter();
    }

    private static void streamOp() throws IOException {
        final String INPUT = "/tmp/test.in";
        final String OUTPUT = "/tmp/test.out";

        InputStream is = new BufferedInputStream(new FileInputStream(INPUT));
        OutputStream os = new BufferedOutputStream(new FileOutputStream(OUTPUT));

        int value = -1;
        while ((value = is.read()) != -1) {
            os.write((byte) (value));
        }

        is.close();
        os.close();

        System.out.println("OK!");
    }

    private static void charOp() throws IOException {
        final String INPUT = "/tmp/data.char.in";
        final String OUTPUT = "/tmp/data.char.out";

        Reader reader = new BufferedReader(new FileReader(INPUT));
        Writer writer = new BufferedWriter(new FileWriter(OUTPUT));

        int value = -1;
        while ((value = reader.read()) != -1) {
            writer.write(value);
        }

        reader.close();
        writer.close();

        System.out.println("OK!");

    }

    private static void objOp() throws IOException, ClassNotFoundException {
        Student student1 = new Student();
        student1.setGrade(5);
        student1.setName("Joseph");
        student1.setAge(27);

        Student student2 = new Student();
        student2.setGrade(15);
        student2.setName("Rose");
        student2.setAge(37);

        final String FILENAME = "/tmp/data.obj";

        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILENAME)));
        oos.writeObject(student1);
        oos.writeObject(student2);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(FILENAME)));
        Student studObj1 = (Student) ois.readObject();
        Student studObj2 = (Student) ois.readObject();
        ois.close();

        System.out.println(studObj1.getName());
        System.out.println(studObj1.getAge());
        System.out.println(studObj1.getGrade());
        System.out.println(studObj2.getName());
        System.out.println(studObj2.getAge());
        System.out.println(studObj2.getGrade());
    }

    private static void dataOp() throws IOException {
        int i = 10;
        float f = 15.2f;
        long l = 19L;

        final String FILENAME = "/tmp/data.out";

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(FILENAME)));

        dos.writeInt(i);
        dos.writeFloat(f);
        dos.writeLong(l);

        dos.close();

        DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(FILENAME)));

        System.out.println(dis.readInt());
        System.out.println(dis.readFloat());
        System.out.println(dis.readLong());

        dis.close();
    }

    private static void formatter() throws FileNotFoundException {
        final String FILENAME = "/tmp/data.formatter";

        Formatter formatter = new Formatter(new BufferedOutputStream(new FileOutputStream(FILENAME)));

        final int COUNT = 10;
        for (int i = 0; i < COUNT; i++) {
            formatter.format("帅哥%s号: %d. %n", i, i + 20);
        }

        formatter.close();
    }

    private static void scanner() throws FileNotFoundException {
        final String FILENAME = "/tmp/data.scan";

        Scanner scanner = new Scanner(new BufferedInputStream(new FileInputStream(FILENAME)));

        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }

        scanner.close();
    }
}
