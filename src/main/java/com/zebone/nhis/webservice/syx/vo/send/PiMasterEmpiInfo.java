package com.zebone.nhis.webservice.syx.vo.send;

import java.util.List;

public class PiMasterEmpiInfo {
      private String oldPkPi ;
      private String oldMpi ;
      /// <summary>
      /// 患者编码
      /// </summary>
      private String CodePi;
      /// <summary>
      /// 患者主键
      /// </summary>
		private String PkPi;
      /// <summary>
      /// 主索引号
      /// </summary>
		private String Mpi;
      /// <summary>
      /// 患者姓名
      /// </summary>
		private String NamePi;
      /// <summary>
      /// 性别代码
      /// </summary>
		private String CodeSex;
      /// <summary>
      /// 性别
      /// </summary>
		private String NameSex;
      /// <summary>
      /// 出生日期
      /// </summary>
		private String Birthday;
      /// <summary>
      /// 医保类型编码
      /// </summary>
		private String CodeHpType;
      /// <summary>
      /// 医保类型名称
      /// </summary>
		private String NameHpType;
      /// <summary>
      /// 婚姻状况编码
      /// </summary>
		private String CodeMari;
      /// <summary>
      /// 婚姻状况名称
      /// </summary>
		private String NameMari;
      /// <summary>
      /// 职业类别编码
      /// </summary>
		private String CodeOccu;
      /// <summary>
      /// 职业类别名称
      /// </summary>
		private String NameOccu;
      /// <summary>
      /// ABO血型代码
      /// </summary>
		private String BloodType;
      /// <summary>
      /// RH血型代码
      /// </summary>
		private String Rh;
      /// <summary>
      /// 学历编码
      /// </summary>
		private String CodeEdu;
		private String NameEdu;
		private String CodeDegree;
		private String NameDegree;
		private String CodeCountry;
		private String NameCountry;
		private String CodeNation;
		private String NameNation;
		private String Teleno;
		private String Mobileno;
		private String Email;
		private String NameWoekunit;
		private String AddrWorkunit;
		private String PhoneWorkunit;
		private String PostcodeWorkunit;
		private String CreateDate;
		private String CodeCreateuser;
		private String NameCreateuser;
		private String ExtData;
		private String LevelSecrecy ;

		private List<CardVo> CardList;

		private List<AddressVo> AddressList ;

		private List<Linkman> LinkmanList ;

		public String getOldPkPi() {
			return oldPkPi;
		}

		public void setOldPkPi(String oldPkPi) {
			this.oldPkPi = oldPkPi;
		}

		public String getOldMpi() {
			return oldMpi;
		}

		public void setOldMpi(String oldMpi) {
			this.oldMpi = oldMpi;
		}

		public String getCodePi() {
			return CodePi;
		}

		public void setCodePi(String codePi) {
			CodePi = codePi;
		}

		public String getPkPi() {
			return PkPi;
		}

		public void setPkPi(String pkPi) {
			PkPi = pkPi;
		}

		public String getMpi() {
			return Mpi;
		}

		public void setMpi(String mpi) {
			Mpi = mpi;
		}

		public String getNamePi() {
			return NamePi;
		}

		public void setNamePi(String namePi) {
			NamePi = namePi;
		}

		public String getCodeSex() {
			return CodeSex;
		}

		public void setCodeSex(String codeSex) {
			CodeSex = codeSex;
		}

		public String getNameSex() {
			return NameSex;
		}

		public void setNameSex(String nameSex) {
			NameSex = nameSex;
		}

		public String getBirthday() {
			return Birthday;
		}

		public void setBirthday(String birthday) {
			Birthday = birthday;
		}

		public String getCodeHpType() {
			return CodeHpType;
		}

		public void setCodeHpType(String codeHpType) {
			CodeHpType = codeHpType;
		}

		public String getNameHpType() {
			return NameHpType;
		}

		public void setNameHpType(String nameHpType) {
			NameHpType = nameHpType;
		}

		public String getCodeMari() {
			return CodeMari;
		}

		public void setCodeMari(String codeMari) {
			CodeMari = codeMari;
		}

		public String getNameMari() {
			return NameMari;
		}

		public void setNameMari(String nameMari) {
			NameMari = nameMari;
		}

		public String getCodeOccu() {
			return CodeOccu;
		}

		public void setCodeOccu(String codeOccu) {
			CodeOccu = codeOccu;
		}

		public String getNameOccu() {
			return NameOccu;
		}

		public void setNameOccu(String nameOccu) {
			NameOccu = nameOccu;
		}

		public String getBloodType() {
			return BloodType;
		}

		public void setBloodType(String bloodType) {
			BloodType = bloodType;
		}

		public String getRh() {
			return Rh;
		}

		public void setRh(String rh) {
			Rh = rh;
		}

		public String getCodeEdu() {
			return CodeEdu;
		}

		public void setCodeEdu(String codeEdu) {
			CodeEdu = codeEdu;
		}

		public String getNameEdu() {
			return NameEdu;
		}

		public void setNameEdu(String nameEdu) {
			NameEdu = nameEdu;
		}

		public String getCodeDegree() {
			return CodeDegree;
		}

		public void setCodeDegree(String codeDegree) {
			CodeDegree = codeDegree;
		}

		public String getNameDegree() {
			return NameDegree;
		}

		public void setNameDegree(String nameDegree) {
			NameDegree = nameDegree;
		}

		public String getCodeCountry() {
			return CodeCountry;
		}

		public void setCodeCountry(String codeCountry) {
			CodeCountry = codeCountry;
		}

		public String getNameCountry() {
			return NameCountry;
		}

		public void setNameCountry(String nameCountry) {
			NameCountry = nameCountry;
		}

		public String getCodeNation() {
			return CodeNation;
		}

		public void setCodeNation(String codeNation) {
			CodeNation = codeNation;
		}

		public String getNameNation() {
			return NameNation;
		}

		public void setNameNation(String nameNation) {
			NameNation = nameNation;
		}

		public String getTeleno() {
			return Teleno;
		}

		public void setTeleno(String teleno) {
			Teleno = teleno;
		}

		public String getMobileno() {
			return Mobileno;
		}

		public void setMobileno(String mobileno) {
			Mobileno = mobileno;
		}

		public String getEmail() {
			return Email;
		}

		public void setEmail(String email) {
			Email = email;
		}

		public String getNameWoekunit() {
			return NameWoekunit;
		}

		public void setNameWoekunit(String nameWoekunit) {
			NameWoekunit = nameWoekunit;
		}

		public String getAddrWorkunit() {
			return AddrWorkunit;
		}

		public void setAddrWorkunit(String addrWorkunit) {
			AddrWorkunit = addrWorkunit;
		}

		public String getPhoneWorkunit() {
			return PhoneWorkunit;
		}

		public void setPhoneWorkunit(String phoneWorkunit) {
			PhoneWorkunit = phoneWorkunit;
		}

		public String getPostcodeWorkunit() {
			return PostcodeWorkunit;
		}

		public void setPostcodeWorkunit(String postcodeWorkunit) {
			PostcodeWorkunit = postcodeWorkunit;
		}

		public String getCreateDate() {
			return CreateDate;
		}

		public void setCreateDate(String createDate) {
			CreateDate = createDate;
		}

		public String getCodeCreateuser() {
			return CodeCreateuser;
		}

		public void setCodeCreateuser(String codeCreateuser) {
			CodeCreateuser = codeCreateuser;
		}

		public String getNameCreateuser() {
			return NameCreateuser;
		}

		public void setNameCreateuser(String nameCreateuser) {
			NameCreateuser = nameCreateuser;
		}

		public String getExtData() {
			return ExtData;
		}

		public void setExtData(String extData) {
			ExtData = extData;
		}

		public String getLevelSecrecy() {
			return LevelSecrecy;
		}

		public void setLevelSecrecy(String levelSecrecy) {
			LevelSecrecy = levelSecrecy;
		}

		public List<CardVo> getCardList() {
			return CardList;
		}

		public void setCardList(List<CardVo> cardList) {
			CardList = cardList;
		}

		public List<AddressVo> getAddressList() {
			return AddressList;
		}

		public void setAddressList(List<AddressVo> addressList) {
			AddressList = addressList;
		}

		public List<Linkman> getLinkmanList() {
			return LinkmanList;
		}

		public void setLinkmanList(List<Linkman> linkmanList) {
			LinkmanList = linkmanList;
		}
		
		
}
