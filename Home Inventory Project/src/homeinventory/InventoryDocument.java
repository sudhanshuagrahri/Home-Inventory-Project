package homeinventory;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

import javax.swing.ImageIcon;

public class InventoryDocument implements Printable {

	@Override
	public int print(Graphics g, PageFormat pf, int index) throws PrinterException {
		Graphics2D g2D = (Graphics2D)g;
		if((index+1)>HomeInventory.lastPage)
			return NO_SUCH_PAGE;
		
		//here we will code what going on each page draw it
		int i,iEnd;
		g2D.setFont(new Font("Arial",Font.BOLD,14));
		g2D.drawString("Home Inventory Items - Page "+String.valueOf(index+1) ,(int)pf.getImageableX(), (int)pf.getImageableY()+25);
		//get starting y
		int dy = (int)g2D.getFont().getStringBounds("S", g2D.getFontRenderContext()).getHeight();
		int y = (int)(pf.getImageableY()+4*dy);
		iEnd = HomeInventory.entriesPerPage*(index+1);
		if(iEnd>HomeInventory.numberEntries)
			iEnd = HomeInventory.numberEntries;
		for(i=0+HomeInventory.entriesPerPage*index;i<iEnd;i++) {
			//Dividing line
			Line2D.Double dividingLine =new Line2D.Double(pf.getImageableX(),y,pf.getImageableX()+pf.getImageableWidth(),y);
			g2D.draw(dividingLine);
			y+=dy;
			g2D.setFont(new Font("Arial",Font.BOLD,12));
			g2D.drawString(HomeInventory.myInventory[i].description,(int)pf.getImageableX(), y);
			y+=dy;
			g2D.setFont(new Font("Arial",Font.PLAIN,12));
			g2D.drawString("Loaction: "+HomeInventory.myInventory[i].location,(int)(pf.getImageableX()+25), y);
			y+=dy;
			if(HomeInventory.myInventory[i].marked)
				g2D.drawString("Item is marked with identifying information",(int)(pf.getImageableX()+25), y);
			else
				g2D.drawString("Item is not marked with identifying information",(int)(pf.getImageableX()+25), y);
			y+=dy;
			g2D.drawString("Serial Number: "+HomeInventory.myInventory[i].serialNumber,(int)(pf.getImageableX()+25), y);
			y+=dy;
			g2D.drawString("Price: $"+HomeInventory.myInventory[i].purchasePrice+", Purchased on: "+HomeInventory.myInventory[i].purchaseDate
					,(int)(pf.getImageableX()+25), y);
			y+=dy;
			g2D.drawString("Purchase Loaction: "+HomeInventory.myInventory[i].purchaseLocation,(int)(pf.getImageableX()+25), y);
			y+=dy;
			g2D.drawString("Note: "+HomeInventory.myInventory[i].note,(int)(pf.getImageableX()+25), y);
			y+=dy;
			try {
				// maintain orginal width and height
				Image inventoryImage = new ImageIcon(HomeInventory.myInventory[i].photoFile).getImage();
				double ratio = (double)(inventoryImage.getWidth(null))/(double)(inventoryImage.getHeight(null));
				g2D.drawImage(inventoryImage, (int)(pf.getImageableX()+25), y, (int)(100*ratio), 100, null);
			}
			catch(Exception e) {
				
			}
			y+=2*dy+100;
			
		}
		return PAGE_EXISTS;
	}

}
