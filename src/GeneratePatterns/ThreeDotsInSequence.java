package GeneratePatterns;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ThreeDotsInSequence {
	static int totalNumOfPatterns = 0;
	static FileWriter fileWriter;
	
	public static void main(String[] args) {
		//Get user input
		Scanner scanInput = new Scanner(System.in);
		
		try {
			fileWriter = new FileWriter("ThreeDotsPatterns.txt");
			
			System.out.println("To find all possible patterns that sequentially connect 3 chosen dots");
			System.out.println("in a 3 x 3 matrix with letter A to I, and their respective index/number:\n");
			System.out.println("A B C\t 1 2 3\nD E F => 4 5 6\nG H I\t 7 8 9\n");
			System.out.println("Enter 3 different dots (1-9), e.g.: 1 9 3");
			System.out.println("(press ENTER or SPACE between each number, then press ENTER again):");
			
			int[] dots = new int[3]; //we only accept 3 dots, hence size 3
			
			for(int i = 0; i < 3; i++) {
				dots[i] = scanInput.nextInt(); //only scan for integers
				if (dots[i] < 1 || dots[i] > 9) { //double confirm the user input is within range
					//if failed, give msg
					System.out.println("Input is invalid. Please enter numbers between 1 and 9.");
					return;
				}
				for (int j = 0; j < i; j++) { //check for duplicate number entered
					if(dots[i] == dots[j]) {
						System.out.println("Numbers entered is repeated. All three numbers have to be different.");
						return;
					}	
				}
				//only for debugging purpose
//				System.out.println("dot" + (i+1) + ": " + dots[i]);
			}
			
			String[] dotsABC = new String[3]; //for storing Letters from the numbers entered
			//Translate numbers entered to letters
			for (int k = 0; k < 3; k++) {
				if (dots[k] == 1)
					dotsABC[k] = "A";
				if (dots[k] == 2)
					dotsABC[k] = "B";
				if (dots[k] == 3)
					dotsABC[k] = "C";
				if (dots[k] == 4)
					dotsABC[k] = "D";
				if (dots[k] == 5)
					dotsABC[k] = "E";
				if (dots[k] == 6)
					dotsABC[k] = "F";
				if (dots[k] == 7)
					dotsABC[k] = "G";
				if (dots[k] == 8)
					dotsABC[k] = "H";
				if (dots[k] == 9)
					dotsABC[k] = "I";
			}
			
			int length = 1;
			listPatterns(dotsABC[0], dotsABC[1], dotsABC[2]);
			
			fileWriter.write("\n Total patterns generated: " + totalNumOfPatterns + "\n");
			fileWriter.close();
			System.out.println("Patterns (in numbers) saved to ThreeDotsPatterns.txt");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static void listPatterns(String dot1, String dot2, String dot3) {
		int[] stack = new int[9];
		boolean[] visited = new boolean[9];
		Arrays.fill(visited, false);  //Fill visited with every elements as false
		
		String[] dotsABC = new String[3];
		dotsABC[0] = dot1;
		dotsABC[1] = dot2;
		dotsABC[2] = dot3;
		
		//translate letters back to numbers again
		int[] dots = new int[3];
		for (int k = 0; k < 3; k++) {
			if (dotsABC[k] == "A")
				dots[k] = 1;
			if (dotsABC[k] == "B")
				dots[k] = 2;
			if (dotsABC[k] == "C")
				dots[k] = 3;
			if (dotsABC[k] == "D")
				dots[k] = 4;
			if (dotsABC[k] == "E")
				dots[k] = 5;
			if (dotsABC[k] == "F")
				dots[k] = 6;
			if (dotsABC[k] == "G")
				dots[k] = 7;
			if (dotsABC[k] == "H")
				dots[k] = 8;
			if (dotsABC[k] == "I")
				dots[k] = 9;
		}
		
		
		int start = dots[0];
		int mid = dots[1];
		int end = dots[2];
		System.out.println("start dot: " + dots[0]);
		System.out.println("mid dot: " + dots[1]);
		System.out.println("end dot: " + dots[2]);
		
		stack[0] = start;
		
		visited[start - 1] = true;
		
		int length = 1;
		
		pattern(stack, visited, length, start, mid, end, false); //as of now length remain = -1
		
		
		
	}
	
	static void pattern(int[] stack, boolean[] visited, int length, int start, int mid, int end, boolean midIncluded) {
		try {
			if (length > 0 && stack[length - 1] == end) {
				if (midIncluded) {
					totalNumOfPatterns++;
					printPattern(stack, length - 1);
				}
				return;
			}
			
			for (int i = 1; i <= 9; i++) {
				if (!visited[i - 1] && checkPath(stack[length - 1], i, visited)) {
					stack[length] = i;
					visited[i - 1] = true;
					pattern(stack, visited, length + 1, start, mid, end, (midIncluded || i == mid));
					visited[i - 1] = false;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static void printPattern(int[] stack, int pivot) throws IOException {
		fileWriter.write(" ");
		for (int i = 0; i < pivot; i++)
			fileWriter.write(stack[i] + " > ");
		fileWriter.write(stack[pivot] + "\n");
	}
	
	static boolean checkPath(int dot1, int dot2, boolean[] visited) {
		float dot1_x = (dot1 - 1) / 3;
		float dot1_y = (dot1 - 1) % 3;
		float dot2_x = (dot2 - 1) / 3;
		float dot2_y = (dot2 - 1) % 3;
		float x = (dot2_x + dot1_x) / 2;
		float y = (dot2_y + dot1_y) / 2;
		//Check if a intermediate/middle dot exists, if exists, then check if it has been visited
		if (checkIsInteger(y) && checkIsInteger(x)) {
			if (!visited[(int)(3 * x + y)])
				return false; //a middle dot exists and hasn't been visited yet
		}
		return true; //either no middle dot, or with middle dot that has been visited.
	}
	
	static boolean checkIsInteger(float x) {
		//first cast the first x from float to int, then back to float again.
		//then minus it with it original self again, if it originally already is an Integer
		//result should be == 0 (returns true).
		return ((float)(int)x - x) == 0;
	}
}
