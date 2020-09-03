package com.zizhizhan.legacies.serde.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.zizhizhan.legacies.serde.model.Contact;
import com.zizhizhan.legacies.serde.model.User;

public class HessianMarshaller {

	public static void main(String[] args) throws IOException {
		User user = new User();
		user.setId(UUID.randomUUID());
		user.setName("James.Zhan");
		user.setContact(new Contact("zhiqiangzhan@gmail.com", "13798520139", ""));

		HessianMarshaller hm = new HessianMarshaller();

		byte[] bytes = hm.marshal(user);
		System.out.println(new String(bytes, StandardCharsets.ISO_8859_1));
		
		user = (User) hm.unmarshal(bytes);
		System.out.println(user.getId());
	}

	public byte[] marshal(Object message) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		HessianOutput hessianOut = new HessianOutput(outputStream);

		hessianOut.writeObject(message);
		
		System.out.println(outputStream.size());

		return outputStream.toByteArray();
	}

	public Object unmarshal(byte[] messageBytes) throws IOException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(messageBytes);
		HessianInput hessianInput = new HessianInput(inputStream);

		return hessianInput.readObject();
	}

}
