package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("Should provide the path to the source code");
			System.exit(1);
		}

		File file = new File(args[0]);
		if (!file.exists() || !file.isDirectory() || !file.canRead()) {
			System.err.println("Provide a path to an existing readable directory");
			System.exit(2);
		}

		SourceRoot root = new SourceRoot(file.toPath());

		PrivateElementsPrinter printerPrivate = new PrivateElementsPrinter();

		root.parse("", (localPath, absolutePath, result) -> {
			result.ifSuccessful(unit -> unit.accept(printerPrivate, null));
			return SourceRoot.Callback.Result.DONT_SAVE;
		});

	}

}
