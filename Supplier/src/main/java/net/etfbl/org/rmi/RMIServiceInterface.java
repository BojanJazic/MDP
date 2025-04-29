package net.etfbl.org.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import net.etfbl.org.billModel.Bill;

public interface RMIServiceInterface extends Remote{
	double saveBill(Bill bill) throws RemoteException;
}
