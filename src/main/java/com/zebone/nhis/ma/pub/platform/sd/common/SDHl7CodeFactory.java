package com.zebone.nhis.ma.pub.platform.sd.common;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public  class SDHl7CodeFactory implements ProtocolCodecFactory {

        private final SDNTextLineEncoder encoder;
        private final SDNTextLineDecoder decoder;
        final static char endchar = 0x1c+0x0d;
        final static String hl7Endchar = ""+(char)0x1c+(char)0x0d;
        
        public SDHl7CodeFactory() {
            this(Charset.forName("GBK"));
        }
        public SDHl7CodeFactory(Charset charset) {
        	encoder = new SDNTextLineEncoder(charset, hl7Endchar);   
	        decoder = new SDNTextLineDecoder(charset, hl7Endchar);
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
