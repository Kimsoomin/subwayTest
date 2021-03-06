package com.dabeeo.hanhayou.beans;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dabeeo.hanhayou.map.BlinkingCommon;

public class ProductDetailBean
{
  public String productId;
  public String productName;
  public ArrayList<MediaUrl> mediaUrlArray;
  public int salePrice;
  public String currencyCd;
  public int discountPrice;
  public String saleRate;
  public String origin;
  public String manufacture;
  public String cnName;
  public String cnBrandName;
  public String cnOrigin;
  public String translationYn;
  public boolean tempOut;
  public String designatedShippingYn;
  public String productDetailInfo;
  public ArrayList<InformationDesc> inforDescArray;
  public float currencyConvert;  
  public ArrayList<ProductOptionInfo> productOptionArr;
  public JSONObject productOptionList;
  
  public void setJSONObject(JSONObject obj)
  {
    try
    { 
      if(obj.has("product_option_info"))
      {
        productOptionArr = new ArrayList<ProductOptionInfo>();
        JSONArray objArr = obj.getJSONArray("product_option_info");
        for(int productOptionPosition = 0; productOptionPosition < objArr.length(); productOptionPosition++)
        {
          JSONObject productOptionObj = objArr.getJSONObject(productOptionPosition);
          ProductOptionInfo productInfo = new ProductOptionInfo();
          productInfo.setJsonObject(productOptionObj);
          productOptionArr.add(productInfo);
        }
      }
      
      if(obj.has("product_option_list"))
      {
        productOptionList = obj.getJSONObject("product_option_list");
      }
      
      if(obj.has("product_basic_info"))
      {
        JSONObject productBasicInfo = obj.getJSONObject("product_basic_info");
        
        if(productBasicInfo.has("product_id"))
          productId = productBasicInfo.getString("product_id");
        
        if(productBasicInfo.has("product_nm"))
          productName = productBasicInfo.getString("product_nm");
        
        mediaUrlArray = new ArrayList<MediaUrl>();
        if(productBasicInfo.has("media_url"))
        {
          JSONArray mediaUrlArr = productBasicInfo.getJSONArray("media_url");
          for(int i = 0; i < mediaUrlArr.length() ; i++)
          {
            JSONObject mediaObj = mediaUrlArr.getJSONObject(i);
            MediaUrl mediaurl = new MediaUrl();
            mediaurl.setJsonObject(mediaObj);
            mediaUrlArray.add(mediaurl);
          }
        }
        
        if(productBasicInfo.has("sale_price"))
          salePrice = productBasicInfo.getInt("sale_price");
        
        if(productBasicInfo.has("currency_cd"))
          currencyCd = productBasicInfo.getString("currency_cd");
        
        if(productBasicInfo.has("discount_price"))
        {
          discountPrice = productBasicInfo.getInt("discount_price");
          if(discountPrice == 0)
          {
            discountPrice = salePrice;
          }
        }
        
        if(productBasicInfo.has("sale_rate"))
        {
          saleRate = productBasicInfo.getString("sale_rate");
          if(saleRate.equals("null"))
            saleRate = "0";
        }
        
        if(productBasicInfo.has("origin"))
          origin = productBasicInfo.getString("origin");
        
        if(productBasicInfo.has("manufacture"))
          manufacture = productBasicInfo.getString("manufacture");
        
        if(productBasicInfo.has("cnName"))
          cnName = productBasicInfo.getString("cnName");
        
        if(productBasicInfo.has("cnBrandName"))
          cnBrandName = productBasicInfo.getString("cnBrandName");
        
        if(productBasicInfo.has("cnOrigin"))
          cnOrigin = productBasicInfo.getString("cnOrigin");
        
        if(productBasicInfo.has("translationYn"))
          translationYn = productBasicInfo.getString("translationYn");
        
        if(productBasicInfo.has("tempOut"))
          tempOut = productBasicInfo.getBoolean("tempOut");
        
        if(productBasicInfo.has("designatedShippingYn"))
          designatedShippingYn = productBasicInfo.getString("designatedShippingYn");
      }
      
      if(obj.has("product_detail_info"))
      {
        JSONObject jsonObj = obj.getJSONObject("product_detail_info");
        
        if(jsonObj.has("product_desc"))
          productDetailInfo = jsonObj.getString("product_desc");
        
        if(jsonObj.has("information_desc"))
        {
          inforDescArray = new ArrayList<InformationDesc>();
          JSONArray informationArr = jsonObj.getJSONArray("information_desc");
          for(int i = 0; i < informationArr.length(); i++)
          {
            JSONObject informationObj = informationArr.getJSONObject(i);
            InformationDesc inforDesc = new InformationDesc();
            inforDesc.setJsonObject(informationObj);
            inforDescArray.add(inforDesc);
          }
        }
      }
      
      if(obj.has("CurrencyConvert"))
        currencyConvert = (float)obj.getDouble("CurrencyConvert");      
    }
    catch (Exception e)
    {
      BlinkingCommon.smlLibPrintException("ProductDetailBean", " e : " + e);
    }
  }
  
  public class ProductOptionInfo{
    public String optionAtrributeNm;
    public ArrayList<OptionAttributeVal> optionAttributeArr;
    
    public void setJsonObject(JSONObject obj)
    {
      try
      {
        if(obj.has("option_attribute_nm"))
        {
          optionAtrributeNm = obj.getString("option_attribute_nm");
        }
        
        if(obj.has("option_attribute_val"))
        {
          optionAttributeArr = new ArrayList<OptionAttributeVal>();
          JSONArray objArr = obj.getJSONArray("option_attribute_val");
          for(int attibuteValPositon = 0; attibuteValPositon < objArr.length(); attibuteValPositon++)
          {
            JSONObject attributeValObj = objArr.getJSONObject(attibuteValPositon);
            OptionAttributeVal optionVal = new OptionAttributeVal();
            optionVal.setJsonObjcet(attributeValObj);
            optionAttributeArr.add(optionVal);
          }
        }
      }
      catch (Exception e)
      {
        BlinkingCommon.smlLibPrintException("ProductDetailBean", " e :" + e);
      }
    }
    
    public class OptionAttributeVal
    {
      public String optionId;
      public String attributeNm;
      
      public void setJsonObjcet(JSONObject obj)
      {
        try
        {
          if(obj.has("option_id"))
            optionId = obj.getString("option_id");
          
          if(obj.has("attribute_nm"))
            attributeNm = obj.getString("attribute_nm");
        }
        catch (Exception e)
        {
          BlinkingCommon.smlLibPrintException("ProductDetailBean", " e :" + e);
        }
      }
    }
  }
  
  public class MediaUrl{
    public String altText;
    public String title;
    public String url;
    
    public void setJsonObject(JSONObject obj)
    {
      try
      {
        if(obj.has("altText"))
          altText = obj.getString("altText");
        
        if(obj.has("title"))
          title = obj.getString("title");
        
        if(obj.has("url"))
          url = obj.getString("url");
      }
      catch (Exception e)
      {
        BlinkingCommon.smlLibDebug("MediaUrl", " e : " + e);
      }
    }
  }
  
  public class InformationDesc{
    public String name;
    public String conetent;
    public int displayOrder;
    
    public void setJsonObject(JSONObject obj)
    {
      try
      {
        if(obj.has("name"))
          name = obj.getString("name");
        
        if(obj.has("content"))
          conetent = obj.getString("content");
        
        if(obj.has("displayOrder"))
          displayOrder = obj.getInt("displayOrder");
      }
      catch (Exception e)
      {
        BlinkingCommon.smlLibPrintException("InfomationDesc", " e : " + e);
      }
      
    }
  }
}
