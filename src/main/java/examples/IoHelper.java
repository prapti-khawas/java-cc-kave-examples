/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.io.Directory;
import cc.kave.commons.utils.io.IReadingArchive;
import cc.kave.commons.utils.io.ReadingArchive;

/**
 * this class explains how contexts can be read from the file system
 */
public class IoHelper {

	public static Context readFirstContext(String dir) {
		for (String zip : findAllZips(dir)) {
			List<Context> ctxs = read(zip);
			return ctxs.get(0);
		}
		return null;
	}

	public static List<Context> readAll(String dir) {
		LinkedList<Context> res = Lists.newLinkedList();

		for (String zip : findAllZips(dir)) {
			res.addAll(read(zip));
		}
		return res;
	}

	public static List<Context> read(String zipFile) {
		LinkedList<Context> res = Lists.newLinkedList();
		try {
			IReadingArchive ra = new ReadingArchive(new File(zipFile));
			while (ra.hasNext()) {
				res.add(ra.getNext(Context.class));
			}
			ra.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * will recursively search for all .zip files in the "dir". The paths
	 * that are returned are relative to "dir".
	 */
	public static Set<String> findAllZips(String dir) {
		return new Directory(dir).findFiles(s -> s.endsWith(".zip"));
	}
	
	public static boolean writeZip(String filepath, String jsonString, String folder, String filename) {
		File file = new File(filepath);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory created :"+ file);
				File file2 = new File(filepath+"/"+folder);
				if (!file2.exists()) 
					if (file2.mkdir()) {
						System.out.println("Directory created :"+ file2);
					}
			} else {
				System.out.println("Unable to create directory");
			}
		} else {
			File file2 = new File(filepath+"/"+folder);
			if (!file2.exists()) {
				if (file2.mkdir()) {
					System.out.println("Directory created :"+ file2);
				}
			} else {
				System.out.println("Appending to Directory:" + file2);
			}
		}
		try (FileWriter fileWriter = new FileWriter(filepath+"/"+folder+"/"+filename+".json")) {
			fileWriter.write(jsonString);
			System.out.println("Successfully Copied JSON Object to File...");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return true;
	}
	
	public static void appendToFile(String filename, String text) {
		File f1 = new File(filename);
		try {
			if(!f1.exists()) {
				f1.createNewFile();
	         }
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
			bw.write(text);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(String filename, String text) {
		File f1 = new File(filename);
		try {
			if(!f1.exists()) {
				f1.createNewFile();
	         }
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename, false));
			bw.write(text);
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}