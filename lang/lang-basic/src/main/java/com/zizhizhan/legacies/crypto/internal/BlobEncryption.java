package com.zizhizhan.legacies.crypto.internal;

import com.zizhizhan.legacies.crypto.CRC32;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

import java.util.List;

@Slf4j
public class BlobEncryption implements Encryption  {

	private static final String HEX_CHAR_STRING = "0123456789ABCDEF";
	private CharJumble charJumble;

	public String encrypt(List<Byte> inputBytes) {
		assert(inputBytes != null);
		int numBytes = inputBytes.size();

		StringBuilder output = new StringBuilder(getOutputBufferSize(numBytes));

		for (int i = 0; i < numBytes; i++) {
			int numRepetitions;
			char out;

			for (numRepetitions = 1; (numRepetitions < 51)
					&& (i < numBytes - 1)
					&& (inputBytes.get(i) == inputBytes.get(i + 1)); numRepetitions++) {
				i++;
			}

			char repeatingByte = getNumRepetitionsAsChar(numRepetitions);

			numRepetitions = addNumRepetitions(output, numRepetitions, repeatingByte);

			short inputByte = getCByte(inputBytes.get(i));

			if (((inputByte >= 'A') && (inputByte <= 'Z')) || ((inputByte >= 'a') && (inputByte <= 'z'))) {
				out = charJumble.jumble((char) inputByte);
			} else if (inputByte < 10) {
				out = (char) ('0' + inputByte);
			} else if (inputByte == ' ') {
				out = '.';
			} else {
				out = 0;
				numRepetitions = addRepeatingSpecialCharacter(output,
						numRepetitions, repeatingByte);
			}

			numRepetitions = addToEncryptedOutput(output, numRepetitions, out, inputByte);
		}

		return ("EX01" + getCRCAsString(inputBytes) + output.toString());

		
	}

	private String getCRCAsString(List<Byte> inputBytes) {
		
		StringBuilder output = new StringBuilder(8);
		int crcValue = getCRC(inputBytes);

		int crcByte = 0;
		int mask = 0xFF000000;
		for (int i = 0; i < 4; i++) {
			crcByte = crcValue & mask;
			crcByte = crcByte >>> (8 * (3 - i));
			mask = mask >>> 8;

			output.append(HEX_CHAR_STRING.charAt(crcByte / 16));
			output.append(HEX_CHAR_STRING.charAt(crcByte % 16));
		}
		return output.toString();
	}

	private int addToEncryptedOutput(StringBuilder output, int numRepetitions, char out, short inputByte) {
		if (out == 0) {
			output.append('$');
			output.append(HEX_CHAR_STRING.charAt(inputByte / 16));
			output.append(HEX_CHAR_STRING.charAt(inputByte % 16));
		} else {
			while (numRepetitions-- > 0) {
				output.append(out);
			}
		}
		return numRepetitions;
	}

	private int addRepeatingSpecialCharacter(StringBuilder output, int numRepetitions, char repeatingByte) {
		if (numRepetitions > 1) {
			output.append('!');
			output.append(repeatingByte);
			numRepetitions = 1;
		}
		return numRepetitions;		
	}

	private int addNumRepetitions(StringBuilder output, int numRepetitions, char repeatingByte) {
		if (numRepetitions > 4) {
			output.append('!');
			output.append(repeatingByte);
			numRepetitions = 1;
		}
		return numRepetitions;
	}

	private char getNumRepetitionsAsChar(int numRepetitions) {
		char repeatingByte;
		if (numRepetitions < 10) {
			repeatingByte = (char) ('0' + numRepetitions);
		} else if (numRepetitions < 36) {
			repeatingByte = (char) ('A' + (numRepetitions - 10));
		} else {
			repeatingByte = (char) ('a' + (numRepetitions - 36));
		}
		return repeatingByte;		
	}

	private int getOutputBufferSize(int inputSize) {
		//	SIGNATURE + brokendown CRC + 3 * input
		return (4 + (4 * 2) + (3 * inputSize));		
	}

	public List<Byte> decrypt(String input) {

		assert(input != null);

		// Reading out bytes as we are interested in raw byte and not charactes
		byte[] inputBytes = input.getBytes();

		// Signature(4) + CRC(8, dword's 8 nibbles written as a byte each) is 12
		assert(inputBytes.length > 12);
		assert(inputBytes[0] == 'E' && inputBytes[1] == 'X' 
			&& inputBytes[2] == '0' && inputBytes[3] == '1');

		// intialize size, with decrypt size can actually increase/decrease
		List<Byte> output = new ArrayList<Byte>(inputBytes.length);

		int receivedCRC = getEmbeddedCRC(inputBytes);

		// Reading bytes into short to treat them as unsigned
		short inputByte;
		short outputByte;

		int numRepetitions;
		int numBytes = inputBytes.length;
		for (int i = 12; i < numBytes; i++) {
			inputByte = getCByte(inputBytes[i]);
			numRepetitions = 1;

			// read something like !4A
			if ((inputByte == '!') && ((i + 2) < numBytes)) {
				i++;
				numRepetitions = getNumRepetitions(inputBytes, i);
				i++;
			}

			// repeating byte
			inputByte = getCByte(inputBytes[i]);

			if ((inputByte == '$') && ((i + 2) < numBytes)) {
				outputByte = (short) ((decodeHexChar(getCByte(inputBytes[i + 1])) << 4) | decodeHexChar(getCByte(inputBytes[i + 2])));
				i += 2;
			} else if (inputByte >= '0' && inputByte <= '9') {
				outputByte = (short) (inputByte - '0');
			} else if (inputByte == '.') {
				outputByte = ' ';
			} else {
				outputByte = (short) charJumble.unJumble((char) inputByte);
			}

			addToOutput(output, outputByte, numRepetitions);
		}

		return checkCRCAndReturnOutput(output, receivedCRC);
		
	}

	private List<Byte> checkCRCAndReturnOutput(List<Byte> output, int receivedCRC) {
		if (getCRC(output) == receivedCRC) {
			return output;
		} else {
			StringBuilder sb = new StringBuilder(output.size());
			for (Byte b : output) {
				sb.append(b);
			}
			log.error("Encoded input: " + sb.toString() + "received crc: " + receivedCRC);
			return null;
		}
	}
	
	private int getCRC(List<Byte> output) {
		byte[] bArray = new byte[output.size()];

		int i = 0;
		for (byte b : output) {
			bArray[i++] = b;
		}

		return getCRC(bArray);
	}


	private int getCRC(byte[] bArray) {
		CRC32 crc = new CRC32();
		crc.update(bArray, 0, bArray.length);
		return (int) crc.getValue();		
	}

	private void addToOutput(List<Byte> output, short outputByte, int numRepetitions) {
		if (numRepetitions > 1) {
			while (numRepetitions-- > 0) {
				output.add((byte) outputByte);
			}
		} else {
			output.add((byte) outputByte);
		}
		
	}

	private int getNumRepetitions(byte[] inputBytes, int i) {
		short inputByte;
		int numRepetitions;
		inputByte = getCByte(inputBytes[i]);
		if (inputByte <= '9') {
			numRepetitions = inputByte - '0';
		} else if (inputByte < 'a') {
			numRepetitions = inputByte - 'A' + 10;
		} else {
			numRepetitions = inputByte - 'a' + 36;
		}
		return numRepetitions;
	}

	/**
	 * This is used to treat the 8 bits as a c++ unsigned byte. Using java short
	 * to represent the value
	 * 
	 * @param b
	 * @return
	 */
	private short getCByte(byte b) {
		return (short) (b & 0xFF);
	}

	private int getEmbeddedCRC(byte[] inputBytes) {	
		int receivedCRC = 0;
		for (int index = 4; index < 12; index++) {
			receivedCRC = (receivedCRC << 4) | decodeHexChar(inputBytes[index]);
		}
		return receivedCRC;
	}

	private int decodeHexChar(short b) {
		if (b > '9') {
			return b - 'A' + 10;
		} else {
			return b - '0';
		}		
	}
	
	public void setCharJumble(CharJumble charJumble) {
		this.charJumble = charJumble;
	}

}
