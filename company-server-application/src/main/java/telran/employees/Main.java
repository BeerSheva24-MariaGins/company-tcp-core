package telran.employees;

import telran.io.Persistable;
import telran.net.TcpServer;

public class Main {
    private static final String FILE_NAME = "employees.data";
    private static final int PORT = 4000;
    private static final long DEFAULT_INTERVAL_SEC = 10; 
    private static long saveInterval = DEFAULT_INTERVAL_SEC;

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(FILE_NAME);
            
            Thread saveThread = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(saveInterval * 1000);
                        persistable.saveToFile(FILE_NAME);                        
                    } catch (InterruptedException e) {
                        System.out.println("Saving is finished");
                        break;
                    } catch (Exception e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
            });
            saveThread.setDaemon(true); 
            saveThread.start();
        }

        TcpServer tcpServer = new TcpServer(new CompanyProtocol(company), PORT);
        tcpServer.run();
    }
}
