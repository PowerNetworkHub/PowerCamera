package nl.svenar.powercamera.utils;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import nl.svenar.powercamera.PowerCamera;

public class PaginationManager {

    private ArrayList<String> pageItems;
    private String pageTitle;
    private String pageCommand;
    private int currentPage;
    private int numItemsOnPage;
    private String headerMessage;

    public PaginationManager(ArrayList<String> pageItems, String pageTitle, String pageCommand, int currentPage,
            int numItemsOnPage) {
        this.pageItems = pageItems;
        this.pageTitle = pageTitle;
        this.pageCommand = pageCommand;
        this.currentPage = currentPage + 1;
        this.numItemsOnPage = numItemsOnPage;
    }

    public void setHeaderMessage(String headerMessage) {
        this.headerMessage = headerMessage;
    }

    public void send(CommandSender sender) {
        sender.sendMessage(PowerCamera.getInstance().getCommandHeader(this.pageTitle));

        if (headerMessage != null && headerMessage.length() > 0) {
            sender.sendMessage(headerMessage);
            sender.sendMessage("");
        }

        int totalPages = (int) Math.ceil(pageItems.size() / (float) numItemsOnPage);
        currentPage = currentPage < 1 ? 1 : currentPage;
        currentPage = currentPage > totalPages ? totalPages : currentPage;

        int index = 0;
        for (String item : pageItems) {
            if (index > (currentPage - 1) * numItemsOnPage - 1
                    && index < (currentPage - 1) * numItemsOnPage + numItemsOnPage) {
                sender.sendMessage(item);
            }
            index++;
        }

        String previousPageCommand = ChatColor.BLUE + "[" + ChatColor.AQUA + pageCommand + " "
                + (currentPage - 1 < 0 ? 0 : currentPage - 1) + ChatColor.BLUE + "]";
        String nextPageCommand = ChatColor.BLUE + "[" + ChatColor.AQUA + pageCommand + " "
                + (currentPage + 1 > totalPages ? totalPages : currentPage + 1) + ChatColor.BLUE + "]";
        String currentPageInfo = ChatColor.AQUA + String.valueOf(currentPage) + ChatColor.BLUE + "/" + ChatColor.AQUA
                + String.valueOf(totalPages);
        String pageCommandSpacing = "";

        for (int i = 0; i < (PowerCamera.getInstance().getChatMaxLineLength()
                - ChatColor.stripColor(previousPageCommand).length() - ChatColor.stripColor(nextPageCommand).length()
                - ChatColor.stripColor(currentPageInfo).length()) / 2; i++) {
            pageCommandSpacing += " ";
        }
        if (pageCommandSpacing.length() == 0) {
            pageCommandSpacing = "  ";
        }
        sender.sendMessage(
                previousPageCommand + pageCommandSpacing + currentPageInfo + pageCommandSpacing + nextPageCommand);
        sender.sendMessage(PowerCamera.getInstance().getCommandFooter());
    }
}
