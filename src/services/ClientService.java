package services;

import dao.Clients;
import logic.Client;

import java.util.List;

public class ClientService {
    private Clients clients;

    public List<Client> getAllClients() throws Exception {
        return clients.getAllObjects();
    }

    public void addClient(Client c) throws Exception{
        clients.addObject(c);
    }
}
