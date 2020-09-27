package shapezShapes;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class ShapezShapes {
	
	// list of shape codes and list of whether each shape exists
	static ArrayList<Integer> counts = new ArrayList<Integer>();
	static ArrayList<String> shapeList = new ArrayList<String>();
	static String operation = "Start                                                       ";
	static ArrayList<String> operationList = new ArrayList<String>();
	static int[] possibleShapes = new int[65536];
	static int iteration = 0;
	
	public static void main(String[] args) {
		
		Arrays.fill(possibleShapes, -1);
		
		// add full 1-layer shape
		addShape("1111:0000:0000:0000");
		
		int oldOldSize, oldSize = 0;
		while (shapeList.size() != oldSize) {
			iteration++;
			
			oldOldSize = oldSize;
			oldSize = shapeList.size();
			
			counts.add(oldSize);
			System.out.println(oldSize); /**/
			
			// rotating and cutting
			String shape;
			for (int i=oldOldSize; i<oldSize; i++) {
				
				shape = shapeList.get(i);
				
				addShape(rotateCW(shape));
				addShape(rotateCCW(shape));
				addShapes(cut(shape));
				addShapes(cutQuad(shape));
				
			}
			
			// stacking
			for (int i=oldOldSize; i<oldSize; i++) {
				
				shape = shapeList.get(i);
				
				// stacking old and new
				for (int j=0; j<oldOldSize; j++) {
					
					stack(shape, shapeList.get(j));
					stack(shapeList.get(j), shape);
					
				}
				
				// stacking new only
				for (int j=oldOldSize; j<oldSize; j++) {
					
					stack(shape, shapeList.get(j));
					
				}
				
			}
			
		}
		
		// put data into file
		System.out.println("Creating file...");
		try {
			
			File file = new File("ADDRESS HERE");
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(file);
			
			
			// shapes per iteration
			pw.println("Shapes per iteration (Total => Diff):");
			
			// get max text length for sizes and differences
			int maxSize = 0;
			int maxDiff = 0;
			for (int i = 0; i < counts.size(); i++) {
				
				int sizeLen = Integer.toString(counts.get(i)).length();
				int diffLen = Integer.toString(counts.get(i) - ((i == 0) ? 0 : counts.get(i-1))).length();
				
				maxSize = (sizeLen > maxSize) ? sizeLen : maxSize;
				maxDiff = (diffLen > maxDiff) ? diffLen : maxDiff;
				
			}
			
			for (int i = 0; i < counts.size(); i++) {
				
				int size = counts.get(i);
				int diff = size - ((i == 0) ? 0 : counts.get(i-1));
				
				String padSize = "";
				for (int j = 0; j < maxSize - Integer.toString(size).length(); j++) {
					padSize += " ";
				}
				String padDiff = "";
				for (int j = 0; j < maxDiff - Integer.toString(diff).length(); j++) {
					padDiff += " ";
				}
				
				pw.println(i + " iterations: " + padSize + size + " => " + padDiff + diff);
				
			}
			
			
			// shapes per size
			pw.println();
			pw.println("Shapes per size (Plain => True):");
			
			// get size counts
			int[] sizes = new int[17];
			for (int i = 0; i < shapeList.size(); i++) {
				sizes[shapeSize(shapeList.get(i))]++;
			}
			
			// get max text length for plain sizes and true sizes
			int totalF = 0;
			BigInteger totalT = BigInteger.valueOf(0);
			int maxSizeF = 0;
			int maxSizeT = 0;
			for (int i = 0; i < sizes.length; i++) {
				totalF += sizes[i];
				totalT = totalT.add(BigInteger.valueOf(sizes[i]).multiply(BigInteger.valueOf(32).pow(i)));
			}
			maxSizeF = Integer.toString(totalF).length();
			maxSizeT = totalT.toString().length();
			
			// print sizes
			for (int i = 0; i < sizes.length; i++) {
				
				int size = sizes[i];
				BigInteger sizeTrue = BigInteger.valueOf(size).multiply(BigInteger.valueOf(32).pow(i));
				
				String padVal = "";
				for (int j = 0; j < 2 - Integer.toString(i).length(); j++) {
					padVal += " ";
				}
				String padMaxF = "";
				for (int j = 0; j < maxSizeF - Integer.toString(size).length(); j++) {
					padMaxF += " ";
				}
				String padMaxT = "";
				for (int j = 0; j < maxSizeT - sizeTrue.toString().length(); j++) {
					padMaxT += " ";
				}
				
				pw.println(padVal + i + " quarters: " + padMaxF + size + " => " + padMaxT + sizeTrue);
				
			}
			pw.println("Total      : " + totalF + " => " + totalT);
			
			// shapes and operations
			pw.println();
			pw.println("Shapes:");
			
			for (int i = 0; i < shapeList.size(); i++) {
				
				// text
				// add shape and operation to file
				pw.println(operationList.get(i)
					+ " = " + shapeList.get(i)
					+ " (" + possibleShapes[shapeIndex(shapeList.get(i))] + ")"
				);
				
			}
			
			
			pw.close();
			
			System.out.println("Done!");
			
		} catch (IOException e) {
			
			System.out.println("An error occurred.");
			e.printStackTrace();
			
		}
		
	}
	
	public static void addShape(String shape) {
		
		if (possibleShapes[shapeIndex(shape)] == -1) {
			if (shapeSize(shape) == 0) {return;}
			shapeList.add(shape);
			operationList.add(operation);
			possibleShapes[shapeIndex(shape)] = iteration;
			System.out.println(shape); /**/
		}
		
	}
	
	public static void addShapes(String[] shapes) {
		
		for (String shape : shapes) {
			if(possibleShapes[shapeIndex(shape)] == -1) {
				if (shapeSize(shape) == 0) {continue;}
				shapeList.add(shape);
				operationList.add(operation);
				possibleShapes[shapeIndex(shape)] = iteration;
				System.out.println(shape); /**/
			}
		}
		
	}
	
	public static int shapeIndex(String shape) {
		
		char[] shapeChars = shape.toCharArray();
		int index = 0;
		for (int l=0; l<4; l++) {
			for (int q=0; q<4; q++) {
				if (shapeChars[5*l + q] == '1') {
					index += Math.pow(2, (4*l+q));
				}
			}
		}
		return index;
		
	}
	
	public static int shapeSize(String shape) {
		
		char[] shapeChars = shape.toCharArray();
		int size = 0;
		for (int l=0; l<4; l++) {
			for (int q=0; q<4; q++) {
				if (shapeChars[5*l + q] == '1') {
					size++;
				}
			}
		}
		return size;
		
	}
	
	public static String rotateCW(String shape) {
		
		char[] shapeChars = shape.toCharArray();
		for (int l=0; l<4; l++) {
			char temp = shapeChars[5*l + 3];
			for (int q=3; q>0; q--) {
				shapeChars[5*l + q] = shapeChars[5*l + q-1];
			}
			shapeChars[5*l] = temp;
		}
		operation = "Rotate CW  " + shape + " (" + possibleShapes[shapeIndex(shape)] + ")                          ";
		return String.valueOf(shapeChars);
		
	}
	
	public static String rotateCCW(String shape) {
		
		char[] shapeChars = shape.toCharArray();
		for (int l=0; l<4; l++) {
			char temp = shapeChars[5*l];
			for (int q=0; q<3; q++) {
				shapeChars[5*l + q] = shapeChars[5*l + q+1];
			}
			shapeChars[5*l + 3] = temp;
		}
		operation = "Rotate CCW " + shape + " (" + possibleShapes[shapeIndex(shape)] + ")                          ";
		return String.valueOf(shapeChars);
		
	}
	
	public static String filter(String shape, String quads) {
		
		char[] quadsChars = quads.toCharArray();
		char[] shapeChars = shape.toCharArray();
		int l=0;
		for (int c=0; c<4; c++) {
			
			// filter quadrants
			boolean empty = true;
			for (int q=0; q<4; q++) {
				if (quadsChars[q] == '0') {
					shapeChars[5*l + q] = '0';
				} else if (shapeChars[5*l + q] == '1') {
					empty = false;
				}
			}
			
			// drop layers if empty
			if (empty) {
				for (int m=l+1; m<4; m++) {
					for (int q=0; q<4; q++) {
						shapeChars[5*(m-1) + q] = shapeChars[5*m + q];
					}
				}
				for (int q=0; q<4; q++) {
					shapeChars[15 + q] = '0';
				}
				l--;
			}
			
			l++;
		}
		return String.valueOf(shapeChars);
		
	}
	
	public static String[] cut(String shape) {
		
		operation = "Cut        " + shape + " (" + possibleShapes[shapeIndex(shape)] + ")                          ";
		return new String[] {
			filter(shape,"0011"),
			filter(shape,"1100")
		};
		
	}
	
	public static String[] cutQuad(String shape) {
		
		operation = "Quad Cut   " + shape + " (" + possibleShapes[shapeIndex(shape)] + ")                          ";
		return new String[] {
			filter(shape,"1000"),
			filter(shape,"0100"),
			filter(shape,"0010"),
			filter(shape,"0001")
		};
		
	}
	
	public static void stack(String bShape, String tShape) {
		
		char[] bShapeChars = bShape.toCharArray();
		char[] tShapeChars = tShape.toCharArray();
		
		// find highest layer for each quadrant of bottom shape
		int[] bShapeHigh = {-1, -1, -1, -1};
		for (int l=3; l>=0; l--) {
			for (int q=0; q<4; q++) {
				if (bShapeChars[5*l + q] != '0' && bShapeHigh[q] < l) {
					bShapeHigh[q] = l;
				}
			}
		}
		
		// find lowest layer for each quadrant of top shape
		int[] tShapeLow = {4, 4, 4, 4};
		for (int l=0; l<4; l++) {
			for (int q=0; q<4; q++) {
				if (tShapeChars[5*l + q] != '0' && tShapeLow[q] > l) {
					tShapeLow[q] = l;
				}
			}
		}
		
		// find layer to stack
		Integer[] gaps = new Integer[4];
		for(int q=0; q<4; q++) {
			gaps[q] = tShapeLow[q] - bShapeHigh[q];
		}
		int merge = Math.max(1 - Collections.min(Arrays.asList(gaps)), 0);
		
		// stack shapes
		char[] shapeChars = bShapeChars.clone();
		for (int l=0; l<4-merge; l++) {
			for (int q=0; q<4; q++) {
				if (tShapeChars[5*l + q] == '1') {
					shapeChars[5*(l+merge) + q] = '1';
				}
			}
		}
		
		String shape = String.valueOf(shapeChars);
		if (possibleShapes[shapeIndex(shape)] == -1) {
			operation = "Stack      "
				+ bShape + " (" + possibleShapes[shapeIndex(bShape)] + ") & "
				+ tShape + " (" + possibleShapes[shapeIndex(tShape)] + ")";
			shapeList.add(shape);
			operationList.add(operation);
			possibleShapes[shapeIndex(shape)] = iteration;
			System.out.println(shape); /**/
			System.out.println(operation); /**/
		}
		
		// return String.valueOf(shapeChars);
		
	}

}
