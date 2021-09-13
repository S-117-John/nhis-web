package com.zebone.nhis.ma.pub.platform.zb.comm;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public  class Hl7CodeFactory implements ProtocolCodecFactory {

        private final NTextLineEncoder encoder;
        private final NTextLineDecoder decoder;
        final static char endchar = 0x1c+0x0d;
        final static String hl7Endchar = ""+(char)0x1c+(char)0x0d;
        
        public Hl7CodeFactory() {
            this(Charset.forName("GBK"));
        }
        public Hl7CodeFactory(Charset charset) {
        	encoder = new NTextLineEncoder(charset, hl7Endchar);   
	        decoder = new NTextLineDecoder(charset, hl7Endchar);
        }

        public ProtocolDecoder getDecoder(IoSession session) throws Exception {
            // TODO Auto-generated method stub
            return decoder;
        }
        public ProtocolEncoder getEncoder(IoSession session) throws Exception {
            // TODO Auto-generated method stub
            return encoder;
        }
        public int getEncoderMaxLineLength() {
            return encoder.getMaxLineLength();
        }
        public void setEncoderMaxLineLength(int maxLineLength) {
            encoder.setMaxLineLength(maxLineLength);
        }
        public int getDecoderMaxLineLength() {
            return decoder.getMaxLineLength();
        }
        public void setDecoderMaxLineLength(int maxLineLength) {
            decoder.setMaxLineLength(maxLineLength);
        }


}
