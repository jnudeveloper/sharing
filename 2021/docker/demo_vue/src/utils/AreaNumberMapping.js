var areaCollection = {};
areaCollection["+1"] = {
  areaNo: "+1",
  countryName: {
    en_us: "United States",
    zh_cn: "美 国",
    zh_tw: "美 國",
  },
  img: "https://yunduanxin.net/img/flags/normal/us.png",
};
areaCollection["+7"] = {
  areaNo: "+7",
  countryName: {
    en_us: "Russia",
    zh_cn: "俄罗斯",
    zh_tw: "俄羅斯",
  },
  img: "https://yunduanxin.net/img/flags/normal/ru.png",
};
areaCollection["+48"] = {
  areaNo: "+48",
  countryName: {
    en_us: "Poland",
    zh_cn: "波 兰",
    zh_tw: "波 蘭",
  },
  img: "https://yunduanxin.net/img/flags/normal/pl.png",
};
areaCollection["+49"] = {
  areaNo: "+49",
  countryName: {
    en_us: "Germany",
    zh_cn: "德 国",
    zh_tw: "德 國",
  },
  img: "https://yunduanxin.net/img/flags/normal/de.png",
};
areaCollection["+62"] = {
  areaNo: "+62",
  countryName: {
    en_us: "Indonesia",
    zh_cn: "印度尼西亚",
    zh_tw: "印度尼西亞",
  },
  img: "https://yunduanxin.net/img/flags/normal/id.png",
};
areaCollection["+61"] = {
  areaNo: "+61",
  countryName: {
    en_us: "Australia",
    zh_cn: "澳大利亚",
    zh_tw: "澳大利亞",
  },
  img: "https://yunduanxin.net/img/flags/normal/au.png",
};
areaCollection["+64"] = {
  areaNo: "+64",
  countryName: {
    en_us: "New Zealand",
    zh_cn: "新西兰",
    zh_tw: "紐西蘭",
  },
  img: "https://pic.baike.soso.com/ugc/baikepic2/5823/20210531153244-890431844_png_3600_1800_86265.jpg/300",
};

areaCollection["+60"] = {
  areaNo: "+60",
  countryName: {
    en_us: "Malaysia",
    zh_cn: "马来西亚",
    zh_tw: "馬來西亞",
  },
  img: "https://yunduanxin.net/img/flags/normal/my.png",
};

areaCollection["+63"] = {
  areaNo: "+63",
  countryName: {
    en_us: "Philippines",
    zh_cn: "菲律宾",
    zh_tw: "菲律賓",
  },
  img: "https://yunduanxin.net/img/flags/normal/ph.png",
};

//////
areaCollection["+44"] = {
  areaNo: "+44",
  countryName: {
    en_us: "United Kingdom",
    zh_cn: "英 国",
    zh_tw: "英 國",
  },
  img: "https://yunduanxin.net/img/flags/normal/gb.png",
};

areaCollection["+66"] = {
  areaNo: "+66",
  countryName: {
    en_us: "Thailand",
    zh_cn: "泰 国",
    zh_tw: "泰 國",
  },
  img: "https://yunduanxin.net/img/flags/normal/th.png",
};

areaCollection["+81"] = {
  areaNo: "+81",
  countryName: {
    en_us: "Japan",
    zh_cn: "日 本",
    zh_tw: "日 本",
  },
  img: "https://yunduanxin.net/img/flags/normal/jp.png",
};

areaCollection["+86"] = {
  areaNo: "+86",
  countryName: {
    en_us: "China",
    zh_cn: "中 国",
    zh_tw: "中 國",
  },
  img: "https://yunduanxin.net/img/flags/normal/cn.png",
};
areaCollection["+852"] = {
  areaNo: "+852",
  countryName: {
    en_us: "Hong Kong",
    zh_cn: "香港",
    zh_tw: "香港",
  },
  img: "https://yunduanxin.net/img/flags/normal/hk.png",
};
areaCollection["+84"] = {
  areaNo: "+84",
  countryName: {
    en_us: "Vietnam",
    zh_cn: "越 南",
    zh_tw: "越 南",
  },
  img: "https://yunduanxin.net/img/flags/normal/vn.png",
};

areaCollection["+853"] = {
  areaNo: "+853",
  countryName: {
    en_us: "Macao",
    zh_cn: "澳 门",
    zh_tw: "澳 門",
  },
  img: "https://yunduanxin.net/img/flags/normal/mo.png",
};

const getAreaMapping = (areaNo) => {
  const locale = localStorage.getItem("locale") || "en_us";
  let areaMapping = areaCollection[areaNo];
  if (areaMapping) {
    areaMapping.countryText = areaMapping.countryName[`${locale}`];
  } else {
    areaMapping = {};
    areaMapping.countryText = "UNKNOW";
    areaMapping.areaNo = areaNo;
    areaMapping.img =
      "https://bkimg.cdn.bcebos.com/pic/1c950a7b02087bf487e9eae5fcd3572c11dfcfb1?x-bce-process=image/resize,m_lfit,w_536,limit_1/format,f_jpg";
  }
  return areaMapping;
};

export default {
  getAreaMapping,
};
