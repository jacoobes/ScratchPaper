package com.jacoobes.scratchpaper;
import io.papermc.lib.PaperLib;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Levi Muniz on 7/29/20.
 *
 * @author Copyright (c) Levi Muniz. All Rights Reserved.
 */
public class ScratchPaper extends JavaPlugin {
    @Override
    public void onEnable() {
        PaperLib.suggestPaper(this);
        getLogger().info("EvalCommand plugin enabled!");
        saveDefaultConfig();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("eval")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: /eval <expression>");
                return true;
            }
            var expression = String.join(" ", args);
            try {
                double result = evaluateExpression(expression);
                sender.sendMessage(ChatColor.GREEN + Double.toString(result));
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
            return true;
        }
        return false;
  }
    private double evaluateExpression(String expression) {
        var Lexer = new ExprLexer(CharStreams.fromString(expression));
        var Parser = new ExprParser(new CommonTokenStream(Lexer));
        ParseTree tree = Parser.prog();

        Double answer = new EvalVisitor().visit(tree);
        if(answer == null) {
            return 0;
        }
        return answer;
    }
}
