package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import logic.Client;
import services.ClientService;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterClientsController extends MainController implements Initializable {
    public JFXTextField nameInput;
    public JFXTextField phoneInput;
    public JFXTextField addressInput;
    public JFXTextField emailInput;
    public JFXTextField codeInput;
    public Label titleLabel;
    public JFXButton doneButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addNumberValidator(phoneInput);
        addNumberValidator(codeInput);
        addRequiredValidator(nameInput);
        addRequiredValidator(phoneInput);
        addRequiredValidator(codeInput);

        //Define modo de edicao caso exista um cliente atual
        if (actualClient != null) {
            nameInput.setText(actualClient.getName());
            phoneInput.setText(actualClient.getPhone());
            emailInput.setText(actualClient.getEmail());
            addressInput.setText(actualClient.getAddress());
            codeInput.setText(actualClient.getId() + "");
            codeInput.setDisable(true);

            doneButton.onActionProperty().set(ignored -> {
                showLoader();
                if (nameInput.validate() && phoneInput.validate() && codeInput.validate()) {
                    if (!emailInput.getText().isEmpty()) {
                        if (isEmailValid(emailInput)) {
                            update();
                        } else {
                            showMsg("Digite um email válido");
                        }
                    } else {
                        update();
                    }
                } else showMsg("Preencha todos os campos corretamente");
            });

            actualClient = null;
            titleLabel.setText("Editar Cliente");
        } else {
            doneButton.onActionProperty().set(ignored -> {
                showLoader();
                if (nameInput.validate() && phoneInput.validate() && codeInput.validate()) {
                    if (!emailInput.getText().isEmpty()) {
                        if (isEmailValid(emailInput)) {
                            register();
                        } else {
                            showMsg("Digite um email válido");
                        }
                    } else {
                        register();
                    }
                } else showMsg("Preencha todos os campos corretamente");
            });
        }
    }

    private void update() {
        Task task = new Task() {

            @Override
            protected Integer call() throws Exception {
                new ClientService().updateClient(new Client(nameInput.getText(), addressInput.getText(), emailInput.getText(), phoneInput.getText(), Integer.parseInt(codeInput.getText())));
                return 1;
            }

            @Override
            protected void succeeded() {
                showMsg("Cliente atualizado com sucesso");
                clearMainArea();
                loadCenterUI("/fxml/Clients.fxml");
                hideLoader();
            }

            @Override
            protected void failed() {
                showMsg("Ocorreu um erro ao atualizar o cliente");
                hideLoader();
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    private void register() {
        Task task = new Task() {

            @Override
            protected Integer call() throws Exception {
                new ClientService().addClient(new Client(nameInput.getText(), addressInput.getText(), emailInput.getText(), phoneInput.getText(), Integer.parseInt(codeInput.getText())));
                return 1;
            }

            @Override
            protected void succeeded() {
                showMsg("Cliente cadastrado com sucesso");
                clearMainArea();
                loadCenterUI("/fxml/Clients.fxml");
                hideLoader();
            }

            @Override
            protected void failed() {
                showMsg("Ocorreu um erro ao atualizar o cliente");
                hideLoader();
            }
        };
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}
