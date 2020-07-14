/**
 * @author Andre
 * @date 13/07/2020 10:10:11
 * 
 */
package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewCoontroller implements Initializable {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction()
	{
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction()
	{
		System.out.println("onMenuItemDepartmentAction");
	}
	
	@FXML
	public void onMenuItemAbutAction()
	{
		System.out.println("onMenuItemAbutAction");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		
		
	}

}
