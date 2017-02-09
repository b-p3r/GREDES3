package di.uminho.miei.gredes;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		String seed[] = { "f169b5f1", "44338875", "bc89273a", "d54d9cfd", "ac52a62f", "bc5afd73", "0baeb380",
				"2329318e", "cddee9d1", "8c73ee0b", "57368f6c", "f4bb7a59", "26aab49e", "5b9db31d", "a7cec064",
				"0e398d47", "cab707be", "0a28f375", "6f9307bb", "14b71942", "b5105c81", "d3c3a12d", "77519f29",
				"048e85ff", "7de4ab5d", "a794f55a", "ade71d48", "ebf50c76", "95b6c2fe", "bd0f1f36", "0be95937",
				"e742e40c", "cb3cc480", "68f51dff", "9115c151", "a79a75c8", "dd48243c", "5a7792f7", "08ce5db7",
				"25c7b4bc", "1001cbc3", "805c6db2", "2a35ab51", "a3327fd0", "f0c4d23f", "f48a26bb", "abbd9f66",
				"1b4f748a", "69558b3e", "e612bc30", "c487aa9a", "a22673c1", "29a1495c", "0959fceb", "7310cc44",
				"0a681b80", "d87224bc", "71536e8a", "f5726f9e", "359c6e21", "08b306e1", "829f0b39", "3e2ed27c",
				"bc12132a", "c3a822ef", "bbc81e2f", "5ca9556d", "19d0a1a8", "4bdaaea8", "cce3ffae", "60adc248",
				"83d94e83", "5c47de61", "9004f270", "6fe91a3e", "48b5e105", "d3061c7d", "bc3e7404", "0e51d934",
				"4d83d7fc", "0e6a493b", "20a3622b", "37dad447", "c22fbc55", "1d7cac2c", "a6ead1a2", "35f6e23c",
				"ced2884a", "a7f46c59", "4c28e672", "9ffd3423", "0aba1a60", "fe6741de", "a1547eb7", "6696fc1f",
				"fdbcf1c7", "85426f68", "8097eb6e", "36c11df5", "70fc730a", "8853271e", "6debf13c", "2760f9ff",
				"409dc3fe", "5ed82b21", "8d845107", "d4f109e4", "6981fe4c", "fda2ef91", "12eb1f4d", "b679f92b",
				"1e1d8dca", "7c727e8b", "0902c012", "72ba80be", "6027e79c", "794f77ee", "5983d871", "89720ebb",
				"9db02184", "2546cbf4", "9b8e2cf1", "351a0ee5", "fda60be5", "b4d43389", "8ebd7363", "53adf017",
				"ead0fdaf", "7f653df3", "c1a42e06", "d6c0c9e4", "7fd9a5f9", "d89d0630", "b46246ec", "0de184ff",
				"633a0543", "230e55e1", "d1a4d3d4", "e2eb6c4f", "c329a0fe", "bfe3338f", "96f585e2", "ae04d039",
				"f9675971", "b45aa8ae", "6560c145", "67c9e290", "4ca3674b", "36e8b28f", "2ec8b9ab", "4815170a",
				"37b67ddf", "19bf1f7a", "b55b80d7", "b1356963", "d672da99", "27699c79", "3951a5bf", "3c922e97",
				"76902a45", "985c68e8", "6f333fdd", "54493b97", "2fae0307", "b7bf0598", "a6661141", "1ddbd24b",
				"80bbdd60", "9c3939ab", "aad69686", "5ca79492", "7b1318bb", "9678cd6d", "280a2739", "4bf25fe2",
				"3f93a0f5", "fee6f7a4", "730b1c66", "b21db7b3", "679c2374", "b83f0393", "1717349d", "aa5627e8",
				"73458931", "619d2e48", "0f00af6f", "3c75f8da", "5a44f746", "c442d1de", "b75743a5", "65d61c20",
				"26899754", "91d42d68", "71764295", "9fa3f95c", "fe1659b5", "ee51d430", "04262b5a", "abca7c73",
				"8851c302", "aa56edb6", "3413cb47", "b473818c", "8204cf85", "636ae918", "7edac320", "4aa82f16",
				"4a9a95fa", "e58ab46d", "67ecf74c", "ce56f97a", "f2e96e98", "3f4a815a", "74543d30", "58e28d9b",
				"2f2b7401", "b73ad383", "2f2bd5be", "a10aa6a0", "18d702ea", "3f6d4ac6", "f9d54178", "0648e925",
				"4af1c0aa", "3ffe8c4d", "49cc3c2b", "5f687b14", "81beb1b9", "144210e6", "4185590f", "de55097c",
				"fcea3dc3", "c4aaeed8", "56513511", "af45ac73", "363791f1", "59e88548", "2d9368cd", "1f092f7f",
				"97ee8fed", "daf844da", "7130ac16", "621f9979", "d8b48a01", "f41852fe", "1aa89477", "d7bcd81d",
				"8ad70eaf", "189fd859", "a319cf12", "71517b11", "437f99ff", "b77fad60", "b447befe", "7e061f57",
				"3f8f63ab" };

		String cc = "dfh8ty3t-4rq8549";
		int X = 0;
		int N = 256;
		int D = 8;
		int S1 = 0;
		int S2 = 0;
		int tmp[][] = new int[N][D];

		System.out.println("Binary of getBytes");
		for (int i = 0; i < cc.length(); i++) {
			X += cc.charAt(i);
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < D; j++) {
				tmp[i][j] = Integer.parseInt(seed[i].charAt(j) + "", 16);
				if (((i % 2) == 0) && ((j % 2) == 0)) {
					S1 += Integer.parseInt(seed[i].charAt(j) + "", 16);
				} else if (((i % 2) != 0) && ((j % 2) != 0)) {
					S2 += Integer.parseInt(seed[i].charAt(j) + "", 16);
				}

			}

		}

		int C = (S1 + X) % N;
		int L = (S2 + X) % D;

		System.out.println("X " + X);
		System.out.println("S1 " + S1);
		System.out.println("S2 " + S2);
		System.out.println("C " + C);
		System.out.println("L " + L);

		int m[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		int b[][] = { { 1, 0, 0 }, { 0, 1, 1 }, { 1, 0, 1 } };

		vertical(2, 2, 3, m);

		horizontal(1, 1, 3, m);

		substitution(3, 3, 3, 3, b);
		//
		// for (int l = 0; l < 3; l++) {
		// System.out.print("{");
		//
		// for (int g = 0; g < 3; g++) {
		//
		// System.out.print("\t" + m[l][g] + "\t");
		// }
		// System.out.println("}");
		// }
		//
		for (int l = 0; l < 3; l++) {
			System.out.print("{");

			for (int g = 0; g < 3; g++) {

				System.out.print("\t" + b[l][g] + "\t");
			}
			System.out.println("}");
		}

		 for (int i = 0; i < 3; i++) {
		 System.out.print("{");
		
		 for (int j = 0; j < 3; j++) {
		
		 System.out.print("\t" + m[i][j] + "\t");
		 }
		 System.out.println("}");

		}

		 String[] toReturn = new String[N];
		
		 System.out.println("{");
		 for (int i = 0; i < N; i++) {
		 System.out.print("{");
		 StringBuilder sb = new StringBuilder();
		 for (int j = 0; j < D; j++) {
		 sb.append(Integer.toHexString(tmp[i][j]));
		 System.out.print("\t" + tmp[i][j] + "\t");
		 }
		 System.out.println("}");
		 toReturn[i] = sb.toString();
		
		 }
		 System.out.println("}");
		
		 System.out.println("{");
		 for (int i = 0; i < N; i++) {
		 System.out.println("\t" + toReturn[i] + "\t");
		 }
		 System.out.println("}");
	}

	private static void substitution(int i, int j, int n, int d, int[][] b) {
		i--;
		j--;
		System.out.println("i "+i);
		System.out.println("j "+j);
		
		
		int backcol = 0, frontcol = 0, toprow = 0, belowrow = 0;

		toprow = (i == 0) ? (n - 1) : i - 1;
		belowrow = (i >= (n-1)) ? 0 : i + 1;
		backcol = (j == 0) ? (d - 1) : j - 1;
		frontcol = (j >= (d-1)) ? 0 : j + 1;
		
		System.out.println("toprow "+toprow);

		System.out.println("belowrow "+belowrow);

		System.out.println("backcol "+backcol);

		System.out.println("frontcol "+frontcol);


		ArrayList<Integer> colToCalc = new ArrayList<>();
		ArrayList<Integer> rowToCalc = new ArrayList<>();

		for (int k = 0; k < d; k++) {
            System.out.println("k "+k);
			rowToCalc.add(b[toprow][k] ^ b[belowrow][k]);

		}

		for (int k = 0; k < d; k++) {
			b[i][k] = rowToCalc.get(k);

		}

		for (int k = 0; k < n; k++) {

			colToCalc.add(b[k][backcol] ^ b[k][frontcol]);

		}
		for (int k = 0; k < n; k++) {
			b[k][j] = colToCalc.get(k);

		}

	}

	private static void horizontal(int i, int j, int d, int[][] m) {
		System.out.println("Xs " + j);
		i--;
		j = (j == d || j == 0) ? 0 : Math.abs(d - j);

		System.out.println("Xs " + j);
		ArrayList<Integer> rowToDeslocate = new ArrayList<>();
		for (int k = 0; k < d; k++) {

			System.out.println(j);
			rowToDeslocate.add(m[i][j]);
			j = (j + 1) % d;

		}

		for (int k = 0; k < d; k++) {
			m[i][k] = rowToDeslocate.get(k);

		}

	}

	private static void vertical(int i, int j, int n, int[][] m) {
		System.out.println("Xs " + j);
		i--;
		j = (j == n || j == 0) ? 0 : Math.abs(n - j);

		System.out.println("Xs " + j);
		ArrayList<Integer> columnToDeslocate = new ArrayList<>();
		for (int k = 0; k < n; k++) {

			System.out.println(j);
			columnToDeslocate.add(m[j][i]);
			j = (j + 1) % n;

		}

		for (int k = 0; k < n; k++) {
			m[k][i] = columnToDeslocate.get(k);

		}

	}

}
