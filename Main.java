import java.io.*;
import java.util.*;
import java.sql.Timestamp;

class MyException extends Exception{
    public MyException(String exception)
    {
        super(exception);
    }
}
class Product{
    Product()
    {
        System.out.println("\t\t ...!  Welcome  !... \n");

    }
    Product(String productId , String productName , double productPrice , long productQuantity)
    {
        this.productId = productId;
        this.productName = productName.toLowerCase();
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }
    String productId;
    String productName;
    double productPrice;
    long productQuantity;
}
class Inventory extends Product{
    static HashMap<String , Product> map = new HashMap<String, Product>();
    static int productId = 0;
    private static void addProduct(String name , double price , long quantity)
    {
        try {
            Product obj = new Product("P-ID-" + productId, name, price, quantity);
            map.put("P-ID-" + productId++, obj);
            logGenerate(" New Entry");
        }catch (Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    private static boolean chkIdPresent(String id)
    {
        try {
            if (map.get(id) == null){
                logGenerate(" ID not found ..!");
                return false;
            }
        }catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
        logGenerate(" ID found ..!");
        return true;
    }
    private static String checkNamePresent(String name)
    {
        try {
        for(Map.Entry<String, Product> itr : map.entrySet())
        {
            if(itr.getValue().productName.equals(name)){
                logGenerate(" Name found ..!");
                return itr.getValue().productId;
            }
        }}catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
        logGenerate(" Name not found ..!");
        return "";
    }
    public static void getInputFile()
    {
        try {
            File myObj = new File("src/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] arrOfStr = data.split(",", 5);
                double priceRead = Double.parseDouble(arrOfStr[2]);
                if(priceRead < 0)
                    throw new MyException("Price Cannot be Negative ..!");
                long quantityRead = Long.parseLong(arrOfStr[3]);
                if(quantityRead <= 0)
                    throw new MyException("Quantity can only be Positive ..!");
                if(!chkIdPresent(arrOfStr[0]))
                {
                    Product obj = new Product(arrOfStr[0] , arrOfStr[1] , priceRead , quantityRead);
                    map.put(arrOfStr[0], obj);
                    productId++;
                    logGenerate(" New Entry from file");
                }
            }
            myReader.close();
        }catch (MyException exceptionArised){
            System.out.println("MyException has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }catch (FileNotFoundException exceptionArised) {
            System.out.println("FileNotFound Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    public static void chkValidEntry(String name, double price , long quantity) throws MyException
    {
        try{
        final String namePresent = checkNamePresent(name);
        if(namePresent.equals(""))
        {
            if(price < 0)
                throw new MyException("Price Cannot be Negative ..!");
            if(quantity <= 0)
                throw new MyException("Quantity can only be Positive ..!");
            addProduct(name , price , quantity);
            logGenerate(" Valid user input");
        }
        else
        {
            logGenerate(" Name already exists");
            System.out.println("Name Already Exists in ID => "+ namePresent);
        }}catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    public static synchronized void purchaseProduct(String name , long quantity) throws MyException
    {
        try{
            String fileName = "src/purchase.txt";
            FileWriter myWriter = new FileWriter(fileName , true);
            BufferedWriter appendWriter = new BufferedWriter(myWriter);

            final String namePresent = checkNamePresent(name);
            if(namePresent.equals(""))
            {
                logGenerate(" Product not found");
                System.out.println("Product Not Found ..!!");
            }
            else
            {
                if(quantity <= 0)
                    throw new MyException("Quantity can only be Positive ..!");
                if(quantity > map.get(namePresent).productQuantity)
                {
                    logGenerate(" Input Quantity not satisfied");
                    System.out.println("Only "+ map.get(namePresent).productQuantity + " products are available ..!!");
                }
                else{
                    map.get(namePresent).productQuantity -= quantity;
                    appendWriter.write( " Purchased ...product -> " + name + " ...quantity -> " + quantity + " ...price -> " + (quantity * map.get(namePresent).productPrice));
                    appendWriter.newLine();
                    appendWriter.write(Thread.currentThread().getName() + " is executing ");
                    appendWriter.newLine();
                    System.out.println(Thread.currentThread().getName() + " is executing .....");
                    System.out.println("\t\t  ... Current Available ...");
                    retrieveDetailsId(namePresent);
                    logGenerate(" Product Purchased");
                }
            }
            appendWriter.write("---------------------------------------------------------------------------------------");
            appendWriter.newLine();
            appendWriter.close();
            myWriter.close();
        }catch (IOException exceptionArised){
        System.out.println("IOException has occured => " + exceptionArised);
    }catch (Exception exceptionArised){
        System.out.println("Exception has occured => " + exceptionArised);
    }
    }
    public static void retrieveDetailsId(String id)
    {
        try{
        if(chkIdPresent(id))
        {
            logGenerate(" Details retrieved by Id");
            productDetails(id);
        }
        else
        {
            logGenerate(" Id not found");
            System.out.println("ID Not Found ..!!");
        }}catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    public static void retrieveDetailsName(String name)
    {
        try{
        final String namePresent = checkNamePresent(name);
        if(namePresent.equals(""))
        {
            logGenerate(" Product not found");
            System.out.println("Product Not Found ..!!");
        }
        else
        {
            logGenerate(" Details retrieved by Name");
            productDetails(namePresent);
        }}catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    public static void entireReport()
    {
        try {
            String fileName = "src/entireReport.txt";
            double sum = 0;
            FileWriter myWriter = new FileWriter(fileName , true);
            BufferedWriter appendWriter = new BufferedWriter(myWriter);

            for(Map.Entry<String, Product> itr : map.entrySet()) {
                String report = itr.getValue().productId + "," + itr.getValue().productName + "," + itr.getValue().productPrice + "," + itr.getValue().productQuantity+","+(itr.getValue().productPrice*itr.getValue().productQuantity);
                sum += (itr.getValue().productPrice * itr.getValue().productQuantity);
                appendWriter.write(report);
                appendWriter.newLine();
            }
            appendWriter.write("Total Inventory Amount => "+ sum);
            appendWriter.newLine();
            appendWriter.write("----------------------------------------------");
            appendWriter.newLine();

            appendWriter.close();
            myWriter.close();
            logGenerate(" Entire report submitted");
            System.out.println("Successfully wrote to the file.");
        } catch (IOException exceptionArised) {
            System.out.println("IOException has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            logGenerate(" Exception Catched");
        }
    }
    public static void specificReport(String name)
    {
        final String namePresent = checkNamePresent(name);
        if(namePresent.equals(""))
        {
            logGenerate(" Product not found");
            System.out.println("Product Not Found ..!!");
        }
        else
        {
            try {
                String fileName = "src/specificReport.txt";
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write(namePresent+ "," + name + "," + map.get(namePresent).productPrice + "," + map.get(namePresent).productQuantity+","+(map.get(namePresent).productQuantity*map.get(namePresent).productPrice));
                myWriter.close();
                logGenerate(" Specific report submitted");
                System.out.println("Successfully wrote to the file.");
            }catch (IOException exceptionArised) {
                System.out.println("IOException has occured => " + exceptionArised);
                logGenerate(" Exception Catched");
            }catch(Exception exceptionArised){
                System.out.println("Exception has occured => " + exceptionArised);
                logGenerate(" Exception Catched");
            }
        }
    }
    private static void productDetails(String id)
    {
        System.out.println("\t\t    ...Product Details...");
        System.out.println("Product ID :" + map.get(id).productId);
        System.out.println("Product Name :" + map.get(id).productName);
        System.out.println("Product Price :" + map.get(id).productPrice);
        System.out.println("Product Quantity :" + map.get(id).productQuantity);
        logGenerate(" Product details rendered");
    }
    public static void logGenerate(String logEntry)
    {
        try{
            String fileName = "src/log.txt";
            FileWriter myWriter = new FileWriter(fileName , true);
            BufferedWriter appendWriter = new BufferedWriter(myWriter);
            appendWriter.write( new Timestamp(System.currentTimeMillis()) + " ==>  "+ logEntry ) ;
            appendWriter.newLine();
            appendWriter.close();
            myWriter.close();
        }catch (IOException exceptionArised){
            System.out.println("IOException has occured => " + exceptionArised);
        }catch (Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
        }
    }
}
class PurchaseMultiThread extends Thread{
    String productName;
    long productQuantity;
    PurchaseMultiThread(String productName , long productQuantity)
    {
        this.productName = productName;
        this.productQuantity = productQuantity;
    }
    public void run()
    {
        try{
        Inventory.purchaseProduct(productName , productQuantity);
        }catch(MyException exceptionArised) {
            System.out.println("Caught the exception");
            System.out.println(exceptionArised);
            Inventory.logGenerate(" Exception Catched");
        }
    }
}
public class Main {

    public static void main(String[] args) {
        try {
        Inventory.getInputFile();
        Scanner getInput = new Scanner(System.in);

        int keepAlive = 1 , choice;
        String name;
        double price;
        long quantity;

        while(keepAlive != 0) {

            System.out.println("\nSelect Any one Option");
            System.out.println("1 -> Add new product");
            System.out.println("2 -> Purchase");
            System.out.println("3 -> Get the details");
            System.out.println("4 -> Get the report");
            try {
                choice = getInput.nextInt();
            switch (choice){
                case 1:
                    try{
                    System.out.println("Enter the Product Name:");
                    name = getInput.next();
                    name = name.toLowerCase();

                    System.out.println("Enter the Product Price:");
                    price = getInput.nextDouble();

                    System.out.println("Enter the Product Quantity:");
                    quantity = getInput.nextLong();

                    Inventory.chkValidEntry(name , price , quantity);
                    }catch (MyException exceptionArised) {
                        System.out.println("Caught the exception");
                        Inventory.logGenerate(" Exception Catched");
                        System.out.println(exceptionArised);
                    }catch(Exception exceptionArised){
                        System.out.println("Exception has occured => " + exceptionArised);
                        Inventory.logGenerate(" Exception Catched");
                    }
                    break;
                case 2:
                    try{
                    System.out.println("Enter the Product Name to Purchase:");
                    name = getInput.next();
                    name = name.toLowerCase();
                    System.out.println("Enter the Product Quantity needed:");
                    quantity = getInput.nextLong();

                    Thread threadObject = new PurchaseMultiThread(name , quantity);
                    threadObject.start();
                    //Thread threadObject_2 = new PurchaseMultiThread(name , quantity+1);
                    //threadObject_2.start();

                    Inventory.logGenerate(" Thread "+ Thread.currentThread().getName() + " executed");
                    }catch(Exception exceptionArised){
                        Inventory.logGenerate(" Exception Catched");
                        System.out.println("Exception has occured => " + exceptionArised);
                    }
                    break;
                case 3:
                    System.out.println("\nSelect 1 -> to enter Product ID .. or .. 2 -> to enter Product Name");
                    try{
                    int selectChoice = getInput.nextInt();

                    if(selectChoice == 1){
                        System.out.println("Enter the Product ID:");
                        String id = getInput.next();
                        Inventory.retrieveDetailsId(id);
                    }
                    else if(selectChoice == 2)
                    {
                        System.out.println("Enter the Product Name:");
                        name = getInput.next();
                        name = name.toLowerCase();
                        Inventory.retrieveDetailsName(name);
                    }
                    else{
                        System.out.println("Invalid Entry ...");
                    }}catch(InputMismatchException e){System.out.println("Invalid input");
                        Inventory.logGenerate(" Exception Catched");
                        getInput.nextLine();}
                    break;
                case 4:
                    System.out.println("\n Select 1 -> entire Report .. or ..  2 -> specific Product");
                    try{
                    int reportChoice = getInput.nextInt();

                    if(reportChoice == 1)
                        Inventory.entireReport();
                    else if(reportChoice == 2)
                    {
                        System.out.println("Enter the Product Name:");
                        name = getInput.next();
                        name = name.toLowerCase();
                        Inventory.specificReport(name);
                    }
                    else{
                        System.out.println("Invalid Entry ...");
                    }}catch(InputMismatchException e){System.out.println("Invalid input");
                        Inventory.logGenerate(" Exception Catched");
                        getInput.nextLine();}
                    break;

                    default:System.out.println("Invalid Entry ...");
            }}
            catch(InputMismatchException e){System.out.println("Invalid input");
                Inventory.logGenerate(" Exception Catched");
            getInput.nextLine();}
            System.out.println("\nEnter Any number to continue ... or ... 0 to exit");
            keepAlive = getInput.nextInt();
        }
        }catch(Exception exceptionArised){
            System.out.println("Exception has occured => " + exceptionArised);
            Inventory.logGenerate(" Exception Catched");
        }
        finally {
            System.out.println("\n\t\t ... Execution Completed ...");
            Inventory.logGenerate(" Completed Entire Execution");
        }
    }
}
