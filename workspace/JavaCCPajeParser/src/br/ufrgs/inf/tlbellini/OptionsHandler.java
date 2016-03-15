package br.ufrgs.inf.tlbellini;

import java.sql.SQLException;

import br.ufrgs.inf.tlbellini.plugins.*;
import ml.options.Options;

public class OptionsHandler {

	public Options opt;
	public String filename;
	public String comment = " ";

	public OptionsHandler(String args[]) {
		// get user input
		opt = new Options(args);
		opt.getSet().addOption("f", Options.Separator.BLANK, Options.Multiplicity.ONCE);
		opt.getSet().addOption("m", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("p", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("l", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("s", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("d", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("u", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		opt.getSet().addOption("pwd", Options.Separator.BLANK, Options.Multiplicity.ZERO_OR_ONE);
		// check options
		printOptionsHelper();
	}

	public void printOptionsHelper() {
		// true=ignoreUnmatched false=requireLast
		if (!this.opt.check(true, false)) {
			System.out.println("Your input is incorrect");
			System.out.println("Please use the following notation:");
			System.out.println("-f <path-to-filename>");
			System.out.println("-m <comment> (optional)");
			System.out.println("-p <plugin> (optional, default: null) ");
			System.out.println("The available plugins and its respective inputs are as follows: ");
			System.out.println("pjdump: PajeDump - dumps the data");
			System.out.println(
					"-l <number-of-lines> (optional): number of lines per dump if pjdump chosen, default: 100000");
			System.out.println("mysql: MYSQL Database - sequential batches - insert data into a database using batches");
			System.out.println("-s <server-name> (optional): server name for the database. Default: localhost");
			System.out.println("-u <user-name> (optional): username to access database. Default: root");
			System.out.println("-pwd <password> (optional): password to access database. Default: root");

			System.exit(1);
		}
	}

	public void checkEntry() {
		if (opt.getSet().isSet("f")) {
			String[] entry = opt.getSet().getOption("f").getResultValue(0).split("/");
			filename = entry[entry.length - 1];
			System.out.println(filename);
		}
		if (opt.getSet().isSet("m")) {
			comment = opt.getSet().getOption("m").getResultValue(0);
			System.out.println(comment);
		}
		if (opt.getSet().isSet("p")) {
			switch (opt.getSet().getOption("p").getResultValue(0)) {
			case "pjdump":
				int lines = opt.getSet().isSet("l") ? Integer.parseInt(opt.getSet().getOption("l").getResultValue(0))
						: 1000000;
				PajeGrammar.plugin = new PajeDumpPlugin(lines);
				break;
			case "mysql":
				String serverName = opt.getSet().isSet("s") ? opt.getSet().getOption("s").getResultValue(0)
						: "localhost";
				String database = "paje";
				String username = opt.getSet().isSet("u") ? opt.getSet().getOption("u").getResultValue(0) : "root";
				String password = opt.getSet().isSet("pwd") ? opt.getSet().getOption("pwd").getResultValue(0) : "root";
				try {
					PajeGrammar.plugin = new PajeInsertDBPlugin(serverName, database, username, password);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Invalid plugin. Null chosen.");
				PajeGrammar.plugin = new PajeNullPlugin();
			}
		} else {
			PajeGrammar.plugin = new PajeNullPlugin();
		}
	}

}
