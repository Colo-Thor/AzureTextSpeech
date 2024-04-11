package com.debin.textspeech.util;


import com.debin.textspeech.bean.SpeekItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 来自 https://www.text-to-speech.cn/getSpeekList.php
 */
public class SpeekUtil {
    private static final String speek = "{\"\\u4e2d\\u6587\\uff08\\u4e0a\\u6d77\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"wuu-CN-XiaotongNeural\",\"wuu-CN-YunzheNeural\"],\"LocalName\":[\"\\u6653\\u5f64\",\"\\u4e91\\u54f2\"]},\"\\u4e2d\\u6587\\uff08\\u5e7f\\u4e1c\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"yue-CN-XiaoMinNeural\",\"yue-CN-YunSongNeural\"],\"LocalName\":[\"\\u6653\\u654f\",\"\\u4e91\\u677e\"]},\"\\u4e2d\\u6587\\uff08\\u666e\\u901a\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-XiaoxiaoNeural\",\"zh-CN-YunyangNeural\",\"zh-CN-XiaochenNeural\",\"zh-CN-XiaohanNeural\",\"zh-CN-XiaomoNeural\",\"zh-CN-XiaoqiuNeural\",\"zh-CN-XiaoruiNeural\",\"zh-CN-XiaoshuangNeural\",\"zh-CN-XiaoxuanNeural\",\"zh-CN-XiaoyanNeural\",\"zh-CN-XiaoyouNeural\",\"zh-CN-YunxiNeural\",\"zh-CN-YunyeNeural\",\"zh-CN-XiaomengNeural\",\"zh-CN-XiaoyiNeural\",\"zh-CN-XiaozhenNeural\",\"zh-CN-YunfengNeural\",\"zh-CN-YunhaoNeural\",\"zh-CN-YunjianNeural\",\"zh-CN-YunxiaNeural\",\"zh-CN-YunzeNeural\"],\"LocalName\":[\"\\u6653\\u6653\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u4e91\\u626c\\uff08\\u7537 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u8fb0\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba - \\u6296\\u97f3\\u70ed\\u95e8\\uff09\",\"\\u6653\\u6db5\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u58a8\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u79cb\\uff08\\u5973 - \\u4e2d\\u5e74\\u4eba\\uff09\",\"\\u6653\\u777f\\uff08\\u5973 - \\u8001\\u5e74\\uff09\",\"\\u6653\\u53cc\\uff08\\u5973 - \\u513f\\u7ae5\\uff09\",\"\\u6653\\u8431\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u989c\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u60a0\\uff08\\u5973 - \\u513f\\u7ae5\\uff09\",\"\\u4e91\\u5e0c\\uff08\\u7537 - \\u5e74\\u8f7b\\u4eba - \\u6296\\u97f3\\u70ed\\u95e8\\uff09\",\"\\u4e91\\u91ce\\uff08\\u7537 - \\u4e2d\\u5e74\\u4eba\\uff09\",\"\\u6653\\u68a6\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u6653\\u4f0a\\uff08\\u5973 - \\u513f\\u7ae5\\uff09\",\"\\u6653\\u7504\\uff08\\u5973 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u4e91\\u67ab\\uff08\\u7537 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u4e91\\u7693\\uff08\\u7537 - \\u5e74\\u8f7b\\u4eba\\uff09\",\"\\u4e91\\u5065\\uff08\\u7537 - \\u4e2d\\u5e74\\u4eba\\uff09\",\"\\u4e91\\u590f\\uff08\\u7537 - \\u513f\\u7ae5\\uff09\",\"\\u4e91\\u6cfd\\uff08\\u7537 - \\u4e2d\\u5e74\\u4eba\\uff09\"]},\"\\u4e2d\\u6587\\uff08\\u6cb3\\u5357\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-henan-YundengNeural\"],\"LocalName\":[\"\\u4e91\\u767b\"]},\"\\u4e2d\\u6587\\uff08\\u4e1c\\u5317\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-liaoning-XiaobeiNeural\"],\"LocalName\":[\"\\u6653\\u5317\"]},\"\\u4e2d\\u6587\\uff08\\u9655\\u897f\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-shaanxi-XiaoniNeural\"],\"LocalName\":[\"\\u6653\\u59ae\"]},\"\\u4e2d\\u6587\\uff08\\u5c71\\u4e1c\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-shandong-YunxiangNeural\"],\"LocalName\":[\"\\u4e91\\u7fd4\"]},\"\\u4e2d\\u6587\\uff08\\u56db\\u5ddd\\u8bdd\\uff0c\\u7b80\\u4f53\\uff09\":{\"ShortName\":[\"zh-CN-sichuan-YunxiNeural\"],\"LocalName\":[\"\\u4e91\\u5e0c\"]},\"\\u4e2d\\u6587\\uff08\\u9999\\u6e2f\\u8bdd\\uff0c\\u7e41\\u4f53\\uff09\":{\"ShortName\":[\"zh-HK-HiuMaanNeural\",\"zh-HK-HiuGaaiNeural\",\"zh-HK-WanLungNeural\"],\"LocalName\":[\"\\u66c9\\u66fc\",\"\\u66c9\\u4f73\",\"\\u96f2\\u9f8d\"]},\"\\u4e2d\\u6587\\uff08\\u53f0\\u6e7e\\u8bdd\\uff0c\\u7e41\\u4f53\\uff09\":{\"ShortName\":[\"zh-TW-HsiaoChenNeural\",\"zh-TW-HsiaoYuNeural\",\"zh-TW-YunJheNeural\"],\"LocalName\":[\"\\u66c9\\u81fb\",\"\\u66c9\\u96e8\",\"\\u96f2\\u54f2\"]},\"English (Australia)\":{\"ShortName\":[\"en-AU-NatashaNeural\",\"en-AU-WilliamNeural\",\"en-AU-AnnetteNeural\",\"en-AU-CarlyNeural\",\"en-AU-DarrenNeural\",\"en-AU-DuncanNeural\",\"en-AU-ElsieNeural\",\"en-AU-FreyaNeural\",\"en-AU-JoanneNeural\",\"en-AU-KenNeural\",\"en-AU-KimNeural\",\"en-AU-NeilNeural\",\"en-AU-TimNeural\",\"en-AU-TinaNeural\"],\"LocalName\":[\"Natasha\",\"William\",\"Annette\",\"Carly\",\"Darren\",\"Duncan\",\"Elsie\",\"Freya\",\"Joanne\",\"Ken\",\"Kim\",\"Neil\",\"Tim\",\"Tina\"]},\"English (Canada)\":{\"ShortName\":[\"en-CA-ClaraNeural\",\"en-CA-LiamNeural\"],\"LocalName\":[\"Clara\",\"Liam\"]},\"English (United Kingdom)\":{\"ShortName\":[\"en-GB-LibbyNeural\",\"en-GB-AbbiNeural\",\"en-GB-AlfieNeural\",\"en-GB-BellaNeural\",\"en-GB-ElliotNeural\",\"en-GB-EthanNeural\",\"en-GB-HollieNeural\",\"en-GB-MaisieNeural\",\"en-GB-NoahNeural\",\"en-GB-OliverNeural\",\"en-GB-OliviaNeural\",\"en-GB-ThomasNeural\",\"en-GB-RyanNeural\",\"en-GB-SoniaNeural\",\"en-GB-MiaNeural\"],\"LocalName\":[\"Libby\",\"Abbi\",\"Alfie\",\"Bella\",\"Elliot\",\"Ethan\",\"Hollie\",\"Maisie\",\"Noah\",\"Oliver\",\"Olivia\",\"Thomas\",\"Ryan\",\"Sonia\",\"Mia\"]},\"English (Hongkong)\":{\"ShortName\":[\"en-HK-SamNeural\",\"en-HK-YanNeural\"],\"LocalName\":[\"Sam\",\"Yan\"]},\"English (Ireland)\":{\"ShortName\":[\"en-IE-ConnorNeural\",\"en-IE-EmilyNeural\"],\"LocalName\":[\"Connor\",\"Emily\"]},\"English (India)\":{\"ShortName\":[\"en-IN-NeerjaNeural\",\"en-IN-PrabhatNeural\"],\"LocalName\":[\"Neerja\",\"Prabhat\"]},\"English (Kenya)\":{\"ShortName\":[\"en-KE-AsiliaNeural\",\"en-KE-ChilembaNeural\"],\"LocalName\":[\"Asilia\",\"Chilemba\"]},\"English (Nigeria)\":{\"ShortName\":[\"en-NG-AbeoNeural\",\"en-NG-EzinneNeural\"],\"LocalName\":[\"Abeo\",\"Ezinne\"]},\"English (New Zealand)\":{\"ShortName\":[\"en-NZ-MitchellNeural\",\"en-NZ-MollyNeural\"],\"LocalName\":[\"Mitchell\",\"Molly\"]},\"English (Philippines)\":{\"ShortName\":[\"en-PH-JamesNeural\",\"en-PH-RosaNeural\"],\"LocalName\":[\"James\",\"Rosa\"]},\"English (Singapore)\":{\"ShortName\":[\"en-SG-LunaNeural\",\"en-SG-WayneNeural\"],\"LocalName\":[\"Luna\",\"Wayne\"]},\"English (Tanzania)\":{\"ShortName\":[\"en-TZ-ElimuNeural\",\"en-TZ-ImaniNeural\"],\"LocalName\":[\"Elimu\",\"Imani\"]},\"English (United States)\":{\"ShortName\":[\"en-US-JennyNeural\",\"en-US-JennyMultilingualNeural\",\"en-US-GuyNeural\",\"en-US-AmberNeural\",\"en-US-AnaNeural\",\"en-US-AriaNeural\",\"en-US-AshleyNeural\",\"en-US-BrandonNeural\",\"en-US-ChristopherNeural\",\"en-US-CoraNeural\",\"en-US-DavisNeural\",\"en-US-ElizabethNeural\",\"en-US-EricNeural\",\"en-US-JacobNeural\",\"en-US-JaneNeural\",\"en-US-JasonNeural\",\"en-US-MichelleNeural\",\"en-US-MonicaNeural\",\"en-US-NancyNeural\",\"en-US-SaraNeural\",\"en-US-TonyNeural\",\"en-US-AIGenerate1Neural\",\"en-US-AIGenerate2Neural\",\"en-US-RogerNeural\",\"en-US-SteffanNeural\"],\"LocalName\":[\"Jenny\",\"Jenny Multilingual\",\"Guy\",\"Amber\",\"Ana\",\"Aria\",\"Ashley\",\"Brandon\",\"Christopher\",\"Cora\",\"Davis\",\"Elizabeth\",\"Eric\",\"Jacob\",\"Jane\",\"Jason\",\"Michelle\",\"Monica\",\"Nancy\",\"Sara\",\"Tony\",\"AIGenerate1\",\"AIGenerate2\",\"Roger\",\"Steffan\"]},\"English (South Africa)\":{\"ShortName\":[\"en-ZA-LeahNeural\",\"en-ZA-LukeNeural\"],\"LocalName\":[\"Leah\",\"Luke\"]},\"Afrikaans (South Africa)\":{\"ShortName\":[\"af-ZA-AdriNeural\",\"af-ZA-WillemNeural\"],\"LocalName\":[\"Adri\",\"Willem\"]},\"Amharic (Ethiopia)\":{\"ShortName\":[\"am-ET-AmehaNeural\",\"am-ET-MekdesNeural\"],\"LocalName\":[\"\\u12a0\\u121d\\u1200\",\"\\u1218\\u1245\\u12f0\\u1235\"]},\"Arabic (United Arab Emirates)\":{\"ShortName\":[\"ar-AE-FatimaNeural\",\"ar-AE-HamdanNeural\"],\"LocalName\":[\"\\u0641\\u0627\\u0637\\u0645\\u0629\",\"\\u062d\\u0645\\u062f\\u0627\\u0646\"]},\"Arabic (Bahrain)\":{\"ShortName\":[\"ar-BH-AliNeural\",\"ar-BH-LailaNeural\"],\"LocalName\":[\"\\u0639\\u0644\\u064a\",\"\\u0644\\u064a\\u0644\\u0649\"]},\"Arabic (Algeria)\":{\"ShortName\":[\"ar-DZ-AminaNeural\",\"ar-DZ-IsmaelNeural\"],\"LocalName\":[\"\\u0623\\u0645\\u064a\\u0646\\u0629\",\"\\u0625\\u0633\\u0645\\u0627\\u0639\\u064a\\u0644\"]},\"Arabic (Egypt)\":{\"ShortName\":[\"ar-EG-SalmaNeural\",\"ar-EG-ShakirNeural\"],\"LocalName\":[\"\\u0633\\u0644\\u0645\\u0649\",\"\\u0634\\u0627\\u0643\\u0631\"]},\"Arabic (Iraq)\":{\"ShortName\":[\"ar-IQ-BasselNeural\",\"ar-IQ-RanaNeural\"],\"LocalName\":[\"\\u0628\\u0627\\u0633\\u0644\",\"\\u0631\\u0646\\u0627\"]},\"Arabic (Jordan)\":{\"ShortName\":[\"ar-JO-SanaNeural\",\"ar-JO-TaimNeural\"],\"LocalName\":[\"\\u0633\\u0646\\u0627\\u0621\",\"\\u062a\\u064a\\u0645\"]},\"Arabic (Kuwait)\":{\"ShortName\":[\"ar-KW-FahedNeural\",\"ar-KW-NouraNeural\"],\"LocalName\":[\"\\u0641\\u0647\\u062f\",\"\\u0646\\u0648\\u0631\\u0627\"]},\"Arabic (Lebanon)\":{\"ShortName\":[\"ar-LB-LaylaNeural\",\"ar-LB-RamiNeural\"],\"LocalName\":[\"\\u0644\\u064a\\u0644\\u0649\",\"\\u0631\\u0627\\u0645\\u064a\"]},\"Arabic (Libya)\":{\"ShortName\":[\"ar-LY-ImanNeural\",\"ar-LY-OmarNeural\"],\"LocalName\":[\"\\u0625\\u064a\\u0645\\u0627\\u0646\",\"\\u0623\\u062d\\u0645\\u062f\"]},\"Arabic (Morocco)\":{\"ShortName\":[\"ar-MA-JamalNeural\",\"ar-MA-MounaNeural\"],\"LocalName\":[\"\\u062c\\u0645\\u0627\\u0644\",\"\\u0645\\u0646\\u0649\"]},\"Arabic (Oman)\":{\"ShortName\":[\"ar-OM-AbdullahNeural\",\"ar-OM-AyshaNeural\"],\"LocalName\":[\"\\u0639\\u0628\\u062f\\u0627\\u0644\\u0644\\u0647\",\"\\u0639\\u0627\\u0626\\u0634\\u0629\"]},\"Arabic (Qatar)\":{\"ShortName\":[\"ar-QA-AmalNeural\",\"ar-QA-MoazNeural\"],\"LocalName\":[\"\\u0623\\u0645\\u0644\",\"\\u0645\\u0639\\u0627\\u0630\"]},\"Arabic (Saudi Arabia)\":{\"ShortName\":[\"ar-SA-HamedNeural\",\"ar-SA-ZariyahNeural\"],\"LocalName\":[\"\\u062d\\u0627\\u0645\\u062f\",\"\\u0632\\u0627\\u0631\\u064a\\u0629\"]},\"Arabic (Syria)\":{\"ShortName\":[\"ar-SY-AmanyNeural\",\"ar-SY-LaithNeural\"],\"LocalName\":[\"\\u0623\\u0645\\u0627\\u0646\\u064a\",\"\\u0644\\u064a\\u062b\"]},\"Arabic (Tunisia)\":{\"ShortName\":[\"ar-TN-HediNeural\",\"ar-TN-ReemNeural\"],\"LocalName\":[\"\\u0647\\u0627\\u062f\\u064a\",\"\\u0631\\u064a\\u0645\"]},\"Arabic (Yemen)\":{\"ShortName\":[\"ar-YE-MaryamNeural\",\"ar-YE-SalehNeural\"],\"LocalName\":[\"\\u0645\\u0631\\u064a\\u0645\",\"\\u0635\\u0627\\u0644\\u062d\"]},\"Azerbaijani (Azerbaijan)\":{\"ShortName\":[\"az-AZ-BabekNeural\",\"az-AZ-BanuNeural\"],\"LocalName\":[\"Bab\\u0259k\",\"Banu\"]},\"Bulgarian (Bulgaria)\":{\"ShortName\":[\"bg-BG-BorislavNeural\",\"bg-BG-KalinaNeural\"],\"LocalName\":[\"\\u0411\\u043e\\u0440\\u0438\\u0441\\u043b\\u0430\\u0432\",\"\\u041a\\u0430\\u043b\\u0438\\u043d\\u0430\"]},\"Bangla (Bangladesh)\":{\"ShortName\":[\"bn-BD-NabanitaNeural\",\"bn-BD-PradeepNeural\"],\"LocalName\":[\"\\u09a8\\u09ac\\u09a8\\u09c0\\u09a4\\u09be\",\"\\u09aa\\u09cd\\u09b0\\u09a6\\u09cd\\u09ac\\u09c0\\u09aa\"]},\"Bengali (India)\":{\"ShortName\":[\"bn-IN-BashkarNeural\",\"bn-IN-TanishaaNeural\"],\"LocalName\":[\"\\u09ad\\u09be\\u09b8\\u09cd\\u0995\\u09b0\",\"\\u09a4\\u09be\\u09a8\\u09bf\\u09b6\\u09be\"]},\"Bosnian (Bosnia)\":{\"ShortName\":[\"bs-BA-GoranNeural\",\"bs-BA-VesnaNeural\"],\"LocalName\":[\"Goran\",\"Vesna\"]},\"Catalan (Spain)\":{\"ShortName\":[\"ca-ES-JoanaNeural\",\"ca-ES-AlbaNeural\",\"ca-ES-EnricNeural\"],\"LocalName\":[\"Joana\",\"Alba\",\"Enric\"]},\"Czech (Czech)\":{\"ShortName\":[\"cs-CZ-AntoninNeural\",\"cs-CZ-VlastaNeural\"],\"LocalName\":[\"Anton\\u00edn\",\"Vlasta\"]},\"Welsh (United Kingdom)\":{\"ShortName\":[\"cy-GB-AledNeural\",\"cy-GB-NiaNeural\"],\"LocalName\":[\"Aled\",\"Nia\"]},\"Danish (Denmark)\":{\"ShortName\":[\"da-DK-ChristelNeural\",\"da-DK-JeppeNeural\"],\"LocalName\":[\"Christel\",\"Jeppe\"]},\"German (Austria)\":{\"ShortName\":[\"de-AT-IngridNeural\",\"de-AT-JonasNeural\"],\"LocalName\":[\"Ingrid\",\"Jonas\"]},\"German (Switzerland)\":{\"ShortName\":[\"de-CH-JanNeural\",\"de-CH-LeniNeural\"],\"LocalName\":[\"Jan\",\"Leni\"]},\"German (Germany)\":{\"ShortName\":[\"de-DE-KatjaNeural\",\"de-DE-AmalaNeural\",\"de-DE-BerndNeural\",\"de-DE-ChristophNeural\",\"de-DE-ConradNeural\",\"de-DE-ElkeNeural\",\"de-DE-GiselaNeural\",\"de-DE-KasperNeural\",\"de-DE-KillianNeural\",\"de-DE-KlarissaNeural\",\"de-DE-KlausNeural\",\"de-DE-LouisaNeural\",\"de-DE-MajaNeural\",\"de-DE-RalfNeural\",\"de-DE-TanjaNeural\"],\"LocalName\":[\"Katja\",\"Amala\",\"Bernd\",\"Christoph\",\"Conrad\",\"Elke\",\"Gisela\",\"Kasper\",\"Killian\",\"Klarissa\",\"Klaus\",\"Louisa\",\"Maja\",\"Ralf\",\"Tanja\"]},\"Greek (Greece)\":{\"ShortName\":[\"el-GR-AthinaNeural\",\"el-GR-NestorasNeural\"],\"LocalName\":[\"\\u0391\\u03b8\\u03b7\\u03bd\\u03ac\",\"\\u039d\\u03ad\\u03c3\\u03c4\\u03bf\\u03c1\\u03b1\\u03c2\"]},\"Spanish (Argentina)\":{\"ShortName\":[\"es-AR-ElenaNeural\",\"es-AR-TomasNeural\"],\"LocalName\":[\"Elena\",\"Tomas\"]},\"Spanish (Bolivia)\":{\"ShortName\":[\"es-BO-MarceloNeural\",\"es-BO-SofiaNeural\"],\"LocalName\":[\"Marcelo\",\"Sofia\"]},\"Spanish (Chile)\":{\"ShortName\":[\"es-CL-CatalinaNeural\",\"es-CL-LorenzoNeural\"],\"LocalName\":[\"Catalina\",\"Lorenzo\"]},\"Spanish (Colombia)\":{\"ShortName\":[\"es-CO-GonzaloNeural\",\"es-CO-SalomeNeural\"],\"LocalName\":[\"Gonzalo\",\"Salome\"]},\"Spanish (Costa Rica)\":{\"ShortName\":[\"es-CR-JuanNeural\",\"es-CR-MariaNeural\"],\"LocalName\":[\"Juan\",\"Mar\\u00eda\"]},\"Spanish (Cuba)\":{\"ShortName\":[\"es-CU-BelkysNeural\",\"es-CU-ManuelNeural\"],\"LocalName\":[\"Belkys\",\"Manuel\"]},\"Spanish (Dominican Republic)\":{\"ShortName\":[\"es-DO-EmilioNeural\",\"es-DO-RamonaNeural\"],\"LocalName\":[\"Emilio\",\"Ramona\"]},\"Spanish (Ecuador)\":{\"ShortName\":[\"es-EC-AndreaNeural\",\"es-EC-LuisNeural\"],\"LocalName\":[\"Andrea\",\"Luis\"]},\"Spanish (Spain)\":{\"ShortName\":[\"es-ES-ElviraNeural\",\"es-ES-AbrilNeural\",\"es-ES-AlvaroNeural\",\"es-ES-ArnauNeural\",\"es-ES-DarioNeural\",\"es-ES-EliasNeural\",\"es-ES-EstrellaNeural\",\"es-ES-IreneNeural\",\"es-ES-LaiaNeural\",\"es-ES-LiaNeural\",\"es-ES-NilNeural\",\"es-ES-SaulNeural\",\"es-ES-TeoNeural\",\"es-ES-TrianaNeural\",\"es-ES-VeraNeural\"],\"LocalName\":[\"Elvira\",\"Abril\",\"\\u00c1lvaro\",\"Arnau\",\"Dario\",\"Elias\",\"Estrella\",\"Irene\",\"Laia\",\"Lia\",\"Nil\",\"Saul\",\"Teo\",\"Triana\",\"Vera\"]},\"Spanish (Equatorial Guinea)\":{\"ShortName\":[\"es-GQ-JavierNeural\",\"es-GQ-TeresaNeural\"],\"LocalName\":[\"Javier\",\"Teresa\"]},\"Spanish (Guatemala)\":{\"ShortName\":[\"es-GT-AndresNeural\",\"es-GT-MartaNeural\"],\"LocalName\":[\"Andr\\u00e9s\",\"Marta\"]},\"Spanish (Honduras)\":{\"ShortName\":[\"es-HN-CarlosNeural\",\"es-HN-KarlaNeural\"],\"LocalName\":[\"Carlos\",\"Karla\"]},\"Spanish (Mexico)\":{\"ShortName\":[\"es-MX-DaliaNeural\",\"es-MX-BeatrizNeural\",\"es-MX-CandelaNeural\",\"es-MX-CarlotaNeural\",\"es-MX-CecilioNeural\",\"es-MX-GerardoNeural\",\"es-MX-JorgeNeural\",\"es-MX-LarissaNeural\",\"es-MX-LibertoNeural\",\"es-MX-LucianoNeural\",\"es-MX-MarinaNeural\",\"es-MX-NuriaNeural\",\"es-MX-PelayoNeural\",\"es-MX-RenataNeural\",\"es-MX-YagoNeural\"],\"LocalName\":[\"Dalia\",\"Beatriz\",\"Candela\",\"Carlota\",\"Cecilio\",\"Gerardo\",\"Jorge\",\"Larissa\",\"Liberto\",\"Luciano\",\"Marina\",\"Nuria\",\"Pelayo\",\"Renata\",\"Yago\"]},\"Spanish (Nicaragua)\":{\"ShortName\":[\"es-NI-FedericoNeural\",\"es-NI-YolandaNeural\"],\"LocalName\":[\"Federico\",\"Yolanda\"]},\"Spanish (Panama)\":{\"ShortName\":[\"es-PA-MargaritaNeural\",\"es-PA-RobertoNeural\"],\"LocalName\":[\"Margarita\",\"Roberto\"]},\"Spanish (Peru)\":{\"ShortName\":[\"es-PE-AlexNeural\",\"es-PE-CamilaNeural\"],\"LocalName\":[\"Alex\",\"Camila\"]},\"Spanish (Puerto Rico)\":{\"ShortName\":[\"es-PR-KarinaNeural\",\"es-PR-VictorNeural\"],\"LocalName\":[\"Karina\",\"V\\u00edctor\"]},\"Spanish (Paraguay)\":{\"ShortName\":[\"es-PY-MarioNeural\",\"es-PY-TaniaNeural\"],\"LocalName\":[\"Mario\",\"Tania\"]},\"Spanish (El Salvador)\":{\"ShortName\":[\"es-SV-LorenaNeural\",\"es-SV-RodrigoNeural\"],\"LocalName\":[\"Lorena\",\"Rodrigo\"]},\"Spanish (United States)\":{\"ShortName\":[\"es-US-AlonsoNeural\",\"es-US-PalomaNeural\"],\"LocalName\":[\"Alonso\",\"Paloma\"]},\"Spanish (Uruguay)\":{\"ShortName\":[\"es-UY-MateoNeural\",\"es-UY-ValentinaNeural\"],\"LocalName\":[\"Mateo\",\"Valentina\"]},\"Spanish (Venezuela)\":{\"ShortName\":[\"es-VE-PaolaNeural\",\"es-VE-SebastianNeural\"],\"LocalName\":[\"Paola\",\"Sebasti\\u00e1n\"]},\"Estonian (Estonia)\":{\"ShortName\":[\"et-EE-AnuNeural\",\"et-EE-KertNeural\"],\"LocalName\":[\"Anu\",\"Kert\"]},\"Basque\":{\"ShortName\":[\"eu-ES-AinhoaNeural\",\"eu-ES-AnderNeural\"],\"LocalName\":[\"Ainhoa\",\"Ander\"]},\"Persian (Iran)\":{\"ShortName\":[\"fa-IR-DilaraNeural\",\"fa-IR-FaridNeural\"],\"LocalName\":[\"\\u062f\\u0644\\u0627\\u0631\\u0627\",\"\\u0641\\u0631\\u06cc\\u062f\"]},\"Finnish (Finland)\":{\"ShortName\":[\"fi-FI-SelmaNeural\",\"fi-FI-HarriNeural\",\"fi-FI-NooraNeural\"],\"LocalName\":[\"Selma\",\"Harri\",\"Noora\"]},\"Filipino (Philippines)\":{\"ShortName\":[\"fil-PH-AngeloNeural\",\"fil-PH-BlessicaNeural\"],\"LocalName\":[\"Angelo\",\"Blessica\"]},\"French (Belgium)\":{\"ShortName\":[\"fr-BE-CharlineNeural\",\"fr-BE-GerardNeural\"],\"LocalName\":[\"Charline\",\"Gerard\"]},\"French (Canada)\":{\"ShortName\":[\"fr-CA-SylvieNeural\",\"fr-CA-AntoineNeural\",\"fr-CA-JeanNeural\"],\"LocalName\":[\"Sylvie\",\"Antoine\",\"Jean\"]},\"French (Switzerland)\":{\"ShortName\":[\"fr-CH-ArianeNeural\",\"fr-CH-FabriceNeural\"],\"LocalName\":[\"Ariane\",\"Fabrice\"]},\"French (France)\":{\"ShortName\":[\"fr-FR-AlainNeural\",\"fr-FR-BrigitteNeural\",\"fr-FR-CelesteNeural\",\"fr-FR-ClaudeNeural\",\"fr-FR-CoralieNeural\",\"fr-FR-EloiseNeural\",\"fr-FR-JacquelineNeural\",\"fr-FR-JeromeNeural\",\"fr-FR-JosephineNeural\",\"fr-FR-MauriceNeural\",\"fr-FR-YvesNeural\",\"fr-FR-YvetteNeural\",\"fr-FR-DeniseNeural\",\"fr-FR-HenriNeural\"],\"LocalName\":[\"Alain\",\"Brigitte\",\"Celeste\",\"Claude\",\"Coralie\",\"Eloise\",\"Jacqueline\",\"Jerome\",\"Josephine\",\"Maurice\",\"Yves\",\"Yvette\",\"Denise\",\"Henri\"]},\"Irish (Ireland)\":{\"ShortName\":[\"ga-IE-ColmNeural\",\"ga-IE-OrlaNeural\"],\"LocalName\":[\"Colm\",\"Orla\"]},\"Galicia\":{\"ShortName\":[\"gl-ES-RoiNeural\",\"gl-ES-SabelaNeural\"],\"LocalName\":[\"Roi\",\"Sabela\"]},\"Gujarati (India)\":{\"ShortName\":[\"gu-IN-DhwaniNeural\",\"gu-IN-NiranjanNeural\"],\"LocalName\":[\"\\u0aa7\\u0acd\\u0ab5\\u0aa8\\u0ac0\",\"\\u0aa8\\u0abf\\u0ab0\\u0a82\\u0a9c\\u0aa8\"]},\"Hebrew (Israel)\":{\"ShortName\":[\"he-IL-AvriNeural\",\"he-IL-HilaNeural\"],\"LocalName\":[\"\\u05d0\\u05d1\\u05e8\\u05d9\",\"\\u05d4\\u05d9\\u05dc\\u05d4\"]},\"Hindi (India)\":{\"ShortName\":[\"hi-IN-MadhurNeural\",\"hi-IN-SwaraNeural\"],\"LocalName\":[\"\\u092e\\u0927\\u0941\\u0930\",\"\\u0938\\u094d\\u0935\\u0930\\u093e\"]},\"Croatian (Croatia)\":{\"ShortName\":[\"hr-HR-GabrijelaNeural\",\"hr-HR-SreckoNeural\"],\"LocalName\":[\"Gabrijela\",\"Sre\\u0107ko\"]},\"Hungarian (Hungary)\":{\"ShortName\":[\"hu-HU-NoemiNeural\",\"hu-HU-TamasNeural\"],\"LocalName\":[\"No\\u00e9mi\",\"Tam\\u00e1s\"]},\"Armenian (Armenia)\":{\"ShortName\":[\"hy-AM-AnahitNeural\",\"hy-AM-HaykNeural\"],\"LocalName\":[\"\\u0531\\u0576\\u0561\\u0570\\u056b\\u057f\",\"\\u0540\\u0561\\u0575\\u056f\"]},\"Indonesian (Indonesia)\":{\"ShortName\":[\"id-ID-ArdiNeural\",\"id-ID-GadisNeural\"],\"LocalName\":[\"Ardi\",\"Gadis\"]},\"Icelandic (Iceland)\":{\"ShortName\":[\"is-IS-GudrunNeural\",\"is-IS-GunnarNeural\"],\"LocalName\":[\"Gu\\u00f0r\\u00fan\",\"Gunnar\"]},\"Italian (Italy)\":{\"ShortName\":[\"it-IT-IsabellaNeural\",\"it-IT-ElsaNeural\",\"it-IT-BenignoNeural\",\"it-IT-CalimeroNeural\",\"it-IT-CataldoNeural\",\"it-IT-DiegoNeural\",\"it-IT-FabiolaNeural\",\"it-IT-FiammaNeural\",\"it-IT-GianniNeural\",\"it-IT-ImeldaNeural\",\"it-IT-IrmaNeural\",\"it-IT-LisandroNeural\",\"it-IT-PalmiraNeural\",\"it-IT-PierinaNeural\",\"it-IT-RinaldoNeural\"],\"LocalName\":[\"Isabella\",\"Elsa\",\"Benigno\",\"Calimero\",\"Cataldo\",\"Diego\",\"Fabiola\",\"Fiamma\",\"Gianni\",\"Imelda\",\"Irma\",\"Lisandro\",\"Palmira\",\"Pierina\",\"Rinaldo\"]},\"Japanese (Japan)\":{\"ShortName\":[\"ja-JP-NanamiNeural\",\"ja-JP-KeitaNeural\",\"ja-JP-AoiNeural\",\"ja-JP-DaichiNeural\",\"ja-JP-MayuNeural\",\"ja-JP-NaokiNeural\",\"ja-JP-ShioriNeural\"],\"LocalName\":[\"\\u4e03\\u6d77\",\"\\u572d\\u592a\",\"\\u78a7\\u8863\",\"\\u5927\\u667a\",\"\\u771f\\u5915\",\"\\u76f4\\u7d00\",\"\\u5fd7\\u7e54\"]},\"Javanese (Indonesia)\":{\"ShortName\":[\"jv-ID-DimasNeural\",\"jv-ID-SitiNeural\"],\"LocalName\":[\"Dimas\",\"Siti\"]},\"Georgian (Georgia)\":{\"ShortName\":[\"ka-GE-EkaNeural\",\"ka-GE-GiorgiNeural\"],\"LocalName\":[\"\\u10d4\\u10d9\\u10d0\",\"\\u10d2\\u10d8\\u10dd\\u10e0\\u10d2\\u10d8\"]},\"Kazakh (Kazakhstan)\":{\"ShortName\":[\"kk-KZ-AigulNeural\",\"kk-KZ-DauletNeural\"],\"LocalName\":[\"\\u0410\\u0439\\u0433\\u04af\\u043b\",\"\\u0414\\u04d9\\u0443\\u043b\\u0435\\u0442\"]},\"Khmer (Cambodia)\":{\"ShortName\":[\"km-KH-PisethNeural\",\"km-KH-SreymomNeural\"],\"LocalName\":[\"\\u1796\\u17b7\\u179f\\u17b7\\u178a\\u17d2\\u178b\",\"\\u179f\\u17d2\\u179a\\u17b8\\u1798\\u17bb\\u17c6\"]},\"Kannada (India)\":{\"ShortName\":[\"kn-IN-GaganNeural\",\"kn-IN-SapnaNeural\"],\"LocalName\":[\"\\u0c97\\u0c97\\u0ca8\\u0ccd\",\"\\u0cb8\\u0caa\\u0ccd\\u0ca8\\u0cbe\"]},\"Korean (Korea)\":{\"ShortName\":[\"ko-KR-SunHiNeural\",\"ko-KR-InJoonNeural\",\"ko-KR-BongJinNeural\",\"ko-KR-GookMinNeural\",\"ko-KR-JiMinNeural\",\"ko-KR-SeoHyeonNeural\",\"ko-KR-SoonBokNeural\",\"ko-KR-YuJinNeural\"],\"LocalName\":[\"\\uc120\\ud788\",\"\\uc778\\uc900\",\"\\ubd09\\uc9c4\",\"\\uad6d\\ubbfc\",\"\\uc9c0\\ubbfc\",\"\\uc11c\\ud604\",\"\\uc21c\\ubcf5\",\"\\uc720\\uc9c4\"]},\"Lao (Laos)\":{\"ShortName\":[\"lo-LA-ChanthavongNeural\",\"lo-LA-KeomanyNeural\"],\"LocalName\":[\"\\u0e88\\u0eb1\\u0e99\\u0e97\\u0eb0\\u0ea7\\u0ebb\\u0e87\",\"\\u0ec1\\u0e81\\u0ec9\\u0ea7\\u0ea1\\u0eb0\\u0e99\\u0eb5\"]},\"Lithuanian (Lithuania)\":{\"ShortName\":[\"lt-LT-LeonasNeural\",\"lt-LT-OnaNeural\"],\"LocalName\":[\"Leonas\",\"Ona\"]},\"Latvian (Latvia)\":{\"ShortName\":[\"lv-LV-EveritaNeural\",\"lv-LV-NilsNeural\"],\"LocalName\":[\"Everita\",\"Nils\"]},\"Macedonian (Republic of North Macedonia)\":{\"ShortName\":[\"mk-MK-AleksandarNeural\",\"mk-MK-MarijaNeural\"],\"LocalName\":[\"\\u0410\\u043b\\u0435\\u043a\\u0441\\u0430\\u043d\\u0434\\u0430\\u0440\",\"\\u041c\\u0430\\u0440\\u0438\\u0458\\u0430\"]},\"Malayalam (India)\":{\"ShortName\":[\"ml-IN-MidhunNeural\",\"ml-IN-SobhanaNeural\"],\"LocalName\":[\"\\u0d2e\\u0d3f\\u0d25\\u0d41\\u0d7b\",\"\\u0d36\\u0d4b\\u0d2d\\u0d28\"]},\"Mongolian (Mongolia)\":{\"ShortName\":[\"mn-MN-BataaNeural\",\"mn-MN-YesuiNeural\"],\"LocalName\":[\"\\u0411\\u0430\\u0442\\u0430\\u0430\",\"\\u0415\\u0441\\u04af\\u0439\"]},\"Marathi (India)\":{\"ShortName\":[\"mr-IN-AarohiNeural\",\"mr-IN-ManoharNeural\"],\"LocalName\":[\"\\u0906\\u0930\\u094b\\u0939\\u0940\",\"\\u092e\\u0928\\u094b\\u0939\\u0930\"]},\"Malay (Malaysia)\":{\"ShortName\":[\"ms-MY-OsmanNeural\",\"ms-MY-YasminNeural\"],\"LocalName\":[\"Osman\",\"Yasmin\"]},\"Maltese (Malta)\":{\"ShortName\":[\"mt-MT-GraceNeural\",\"mt-MT-JosephNeural\"],\"LocalName\":[\"Grace\",\"Joseph\"]},\"Burmese (Myanmar)\":{\"ShortName\":[\"my-MM-NilarNeural\",\"my-MM-ThihaNeural\"],\"LocalName\":[\"\\u1014\\u102e\\u101c\\u102c\",\"\\u101e\\u102e\\u101f\"]},\"Norwegian (Bokm\\u00e5l, Norway)\":{\"ShortName\":[\"nb-NO-PernilleNeural\",\"nb-NO-FinnNeural\",\"nb-NO-IselinNeural\"],\"LocalName\":[\"Pernille\",\"Finn\",\"Iselin\"]},\"Nepali (Nepal)\":{\"ShortName\":[\"ne-NP-HemkalaNeural\",\"ne-NP-SagarNeural\"],\"LocalName\":[\"\\u0939\\u0947\\u092e\\u0915\\u0932\\u093e\",\"\\u0938\\u093e\\u0917\\u0930\"]},\"Dutch (Belgium)\":{\"ShortName\":[\"nl-BE-ArnaudNeural\",\"nl-BE-DenaNeural\"],\"LocalName\":[\"Arnaud\",\"Dena\"]},\"Dutch (Netherlands)\":{\"ShortName\":[\"nl-NL-ColetteNeural\",\"nl-NL-FennaNeural\",\"nl-NL-MaartenNeural\"],\"LocalName\":[\"Colette\",\"Fenna\",\"Maarten\"]},\"Polish (Poland)\":{\"ShortName\":[\"pl-PL-AgnieszkaNeural\",\"pl-PL-MarekNeural\",\"pl-PL-ZofiaNeural\"],\"LocalName\":[\"Agnieszka\",\"Marek\",\"Zofia\"]},\"Pashto (Afghanistan)\":{\"ShortName\":[\"ps-AF-GulNawazNeural\",\"ps-AF-LatifaNeural\"],\"LocalName\":[\" \\u06ab\\u0644 \\u0646\\u0648\\u0627\\u0632\",\"\\u0644\\u0637\\u064a\\u0641\\u0647\"]},\"Portuguese (Brazil)\":{\"ShortName\":[\"pt-BR-FranciscaNeural\",\"pt-BR-AntonioNeural\",\"pt-BR-BrendaNeural\",\"pt-BR-DonatoNeural\",\"pt-BR-ElzaNeural\",\"pt-BR-FabioNeural\",\"pt-BR-GiovannaNeural\",\"pt-BR-HumbertoNeural\",\"pt-BR-JulioNeural\",\"pt-BR-LeilaNeural\",\"pt-BR-LeticiaNeural\",\"pt-BR-ManuelaNeural\",\"pt-BR-NicolauNeural\",\"pt-BR-ValerioNeural\",\"pt-BR-YaraNeural\"],\"LocalName\":[\"Francisca\",\"Ant\\u00f4nio\",\"Brenda\",\"Donato\",\"Elza\",\"Fabio\",\"Giovanna\",\"Humberto\",\"Julio\",\"Leila\",\"Leticia\",\"Manuela\",\"Nicolau\",\"Valerio\",\"Yara\"]},\"Portuguese (Portugal)\":{\"ShortName\":[\"pt-PT-DuarteNeural\",\"pt-PT-FernandaNeural\",\"pt-PT-RaquelNeural\"],\"LocalName\":[\"Duarte\",\"Fernanda\",\"Raquel\"]},\"Romanian (Romania)\":{\"ShortName\":[\"ro-RO-AlinaNeural\",\"ro-RO-EmilNeural\"],\"LocalName\":[\"Alina\",\"Emil\"]},\"Russian (Russia)\":{\"ShortName\":[\"ru-RU-SvetlanaNeural\",\"ru-RU-DariyaNeural\",\"ru-RU-DmitryNeural\"],\"LocalName\":[\"\\u0421\\u0432\\u0435\\u0442\\u043b\\u0430\\u043d\\u0430\",\"\\u0414\\u0430\\u0440\\u0438\\u044f\",\"\\u0414\\u043c\\u0438\\u0442\\u0440\\u0438\\u0439\"]},\"Sinhala (Sri Lanka)\":{\"ShortName\":[\"si-LK-SameeraNeural\",\"si-LK-ThiliniNeural\"],\"LocalName\":[\"\\u0dc3\\u0db8\\u0dd3\\u0dbb\",\"\\u0dad\\u0dd2\\u0dc5\\u0dd2\\u0dab\\u0dd2\"]},\"Slovak (Slovakia)\":{\"ShortName\":[\"sk-SK-LukasNeural\",\"sk-SK-ViktoriaNeural\"],\"LocalName\":[\"Luk\\u00e1\\u0161\",\"Vikt\\u00f3ria\"]},\"Slovenian (Slovenia)\":{\"ShortName\":[\"sl-SI-PetraNeural\",\"sl-SI-RokNeural\"],\"LocalName\":[\"Petra\",\"Rok\"]},\"Somali (Somalia)\":{\"ShortName\":[\"so-SO-MuuseNeural\",\"so-SO-UbaxNeural\"],\"LocalName\":[\"Muuse\",\"Ubax\"]},\"Albanian (Albania)\":{\"ShortName\":[\"sq-AL-AnilaNeural\",\"sq-AL-IlirNeural\"],\"LocalName\":[\"Anila\",\"Ilir\"]},\"Serbian (Serbia)\":{\"ShortName\":[\"sr-RS-NicholasNeural\",\"sr-RS-SophieNeural\"],\"LocalName\":[\"\\u041d\\u0438\\u043a\\u043e\\u043b\\u0430\",\"\\u0421\\u043e\\u0444\\u0438\\u0458\\u0430\"]},\"Sundanese (Indonesia)\":{\"ShortName\":[\"su-ID-JajangNeural\",\"su-ID-TutiNeural\"],\"LocalName\":[\"Jajang\",\"Tuti\"]},\"Swedish (Sweden)\":{\"ShortName\":[\"sv-SE-SofieNeural\",\"sv-SE-HilleviNeural\",\"sv-SE-MattiasNeural\"],\"LocalName\":[\"Sofie\",\"Hillevi\",\"Mattias\"]},\"Swahili (Kenya)\":{\"ShortName\":[\"sw-KE-RafikiNeural\",\"sw-KE-ZuriNeural\"],\"LocalName\":[\"Rafiki\",\"Zuri\"]},\"Swahili (Tanzania)\":{\"ShortName\":[\"sw-TZ-DaudiNeural\",\"sw-TZ-RehemaNeural\"],\"LocalName\":[\"Daudi\",\"Rehema\"]},\"Tamil (India)\":{\"ShortName\":[\"ta-IN-PallaviNeural\",\"ta-IN-ValluvarNeural\"],\"LocalName\":[\"\\u0baa\\u0bb2\\u0bcd\\u0bb2\\u0bb5\\u0bbf\",\"\\u0bb5\\u0bb3\\u0bcd\\u0bb3\\u0bc1\\u0bb5\\u0bb0\\u0bcd\"]},\"Tamil (Sri Lanka)\":{\"ShortName\":[\"ta-LK-KumarNeural\",\"ta-LK-SaranyaNeural\"],\"LocalName\":[\"\\u0b95\\u0bc1\\u0bae\\u0bbe\\u0bb0\\u0bcd\",\"\\u0b9a\\u0bb0\\u0ba3\\u0bcd\\u0baf\\u0bbe\"]},\"Tamil (Malaysia)\":{\"ShortName\":[\"ta-MY-KaniNeural\",\"ta-MY-SuryaNeural\"],\"LocalName\":[\"\\u0b95\\u0ba9\\u0bbf\",\"\\u0b9a\\u0bc2\\u0bb0\\u0bcd\\u0baf\\u0bbe\"]},\"Tamil (Singapore)\":{\"ShortName\":[\"ta-SG-AnbuNeural\",\"ta-SG-VenbaNeural\"],\"LocalName\":[\"\\u0b85\\u0ba9\\u0bcd\\u0baa\\u0bc1\",\"\\u0bb5\\u0bc6\\u0ba3\\u0bcd\\u0baa\\u0bbe\"]},\"Telugu (India)\":{\"ShortName\":[\"te-IN-MohanNeural\",\"te-IN-ShrutiNeural\"],\"LocalName\":[\"\\u0c2e\\u0c4b\\u0c39\\u0c28\\u0c4d\",\"\\u0c36\\u0c4d\\u0c30\\u0c41\\u0c24\\u0c3f\"]},\"Thai (Thailand)\":{\"ShortName\":[\"th-TH-PremwadeeNeural\",\"th-TH-AcharaNeural\",\"th-TH-NiwatNeural\"],\"LocalName\":[\"\\u0e40\\u0e1b\\u0e23\\u0e21\\u0e27\\u0e14\\u0e35\",\"\\u0e2d\\u0e31\\u0e08\\u0e09\\u0e23\\u0e32\",\"\\u0e19\\u0e34\\u0e27\\u0e31\\u0e12\\u0e19\\u0e4c\"]},\"Turkish (Turkey)\":{\"ShortName\":[\"tr-TR-AhmetNeural\",\"tr-TR-EmelNeural\"],\"LocalName\":[\"Ahmet\",\"Emel\"]},\"Ukrainian (Ukraine)\":{\"ShortName\":[\"uk-UA-OstapNeural\",\"uk-UA-PolinaNeural\"],\"LocalName\":[\"\\u041e\\u0441\\u0442\\u0430\\u043f\",\"\\u041f\\u043e\\u043b\\u0456\\u043d\\u0430\"]},\"Urdu (India)\":{\"ShortName\":[\"ur-IN-GulNeural\",\"ur-IN-SalmanNeural\"],\"LocalName\":[\"\\u06af\\u0644\",\"\\u0633\\u0644\\u0645\\u0627\\u0646\"]},\"Urdu (Pakistan)\":{\"ShortName\":[\"ur-PK-AsadNeural\",\"ur-PK-UzmaNeural\"],\"LocalName\":[\"\\u0627\\u0633\\u062f\",\"\\u0639\\u0638\\u0645\\u06cc\\u0670\"]},\"Uzbek (Uzbekistan)\":{\"ShortName\":[\"uz-UZ-MadinaNeural\",\"uz-UZ-SardorNeural\"],\"LocalName\":[\"Madina\",\"Sardor\"]},\"Vietnamese (Vietnam)\":{\"ShortName\":[\"vi-VN-HoaiMyNeural\",\"vi-VN-NamMinhNeural\"],\"LocalName\":[\"Ho\\u00e0i My\",\"Nam Minh\"]},\"Zulu (South Africa)\":{\"ShortName\":[\"zu-ZA-ThandoNeural\",\"zu-ZA-ThandoNeural\"],\"LocalName\":[\"Thando\",\"Thando\"]}}";

    public static List<String> getLanguageList() throws Exception {
        String speekInfo = getSpeekInfo();
        speekInfo = speekInfo.substring(speekInfo.indexOf("{") + 1, speekInfo.lastIndexOf("}"));
        List<String> languageList = new ArrayList<>();
        parseLanguage(languageList, speekInfo);
        return languageList;
    }

    private static void parseLanguage(List<String> languageList, String speekInfo) throws Exception {
        int itemIndex = speekInfo.indexOf("}") + 1;
        String item = speekInfo.substring(0, itemIndex);

        int languageIndex = item.indexOf(":");
        languageList.add(item.substring(0, languageIndex).replace("\"", ""));
        if (speekInfo.length() > itemIndex + 1) {
            parseLanguage(languageList, speekInfo.substring(itemIndex + 1));
        }
    }

    public static SpeekItem getSpeekItem(String language) throws Exception {
        String speekInfo = getSpeekInfo();
        speekInfo = speekInfo.substring(speekInfo.indexOf("{") + 1, speekInfo.lastIndexOf("}"));
        return parseSpeekItem(language, speekInfo);
    }

    public static SpeekItem parseSpeekItem(String language, String speekInfo) throws Exception {
        int itemIndex = speekInfo.indexOf("}") + 1;
        String item = speekInfo.substring(0, itemIndex);

        int languageIndex = item.indexOf(":");
        String speekLanguage = item.substring(0, languageIndex).replace("\"", "");
        if (speekLanguage.equals(language)) {
            SpeekItem speekItem = new SpeekItem();
            speekItem.setLanguage(speekLanguage);
            String json = item.substring(languageIndex + 1);
            JSONObject jsonObject = new JSONObject(json);

            String localNames = jsonObject.getString("LocalName");
            String[] localNameArr = localNames.replace("[", "").replace("]", "").split(",");
            List<String> localNameList = new ArrayList<>();
            for (String localName : localNameArr) {
                localNameList.add(localName.replace("\"", ""));
            }
            if ("中文（普通话，简体）".equals(language)) {
                localNameList.add(0, "晓晓（女 - 年轻人 - 多语言）");
            }
            speekItem.setLocalNameList(localNameList);

            String shortNames = jsonObject.getString("ShortName");
            String[] shortNameArr = shortNames.replace("[", "").replace("]", "").split(",");
            List<String> shortNameList = new ArrayList<>();
            for (String shortName : shortNameArr) {
                shortNameList.add(shortName.replace("\"", ""));
            }
            if ("中文（普通话，简体）".equals(language)) {
                shortNameList.add(0, "zh-CN-XiaoxiaoMultilingualNeural");
            }
            speekItem.setShortNameList(shortNameList);
            return speekItem;
        }

        if (speekInfo.length() > itemIndex + 1) {
            return parseSpeekItem(language, speekInfo.substring(itemIndex + 1));
        }

        return null;
    }

    public static List<SpeekItem> parseSpeekInfo() throws Exception {
        String speekInfo = getSpeekInfo();
        speekInfo = speekInfo.substring(speekInfo.indexOf("{") + 1, speekInfo.lastIndexOf("}"));
        List<SpeekItem> speekList = new ArrayList<>();
        parseItem(speekList, speekInfo);
        return speekList;
    }

    private static void parseItem(List<SpeekItem> speekList, String speekInfo) throws Exception {
        SpeekItem speekItem = new SpeekItem();
        int itemIndex = speekInfo.indexOf("}") + 1;
        String item = speekInfo.substring(0, itemIndex);

        int languageIndex = item.indexOf(":");
        speekItem.setLanguage(item.substring(0, languageIndex).replace("\"", ""));
        JSONObject jsonObject = new JSONObject(item.substring(languageIndex + 1));

        String localNames = jsonObject.getString("LocalName");
        String[] localNameArr = localNames.replace("[", "").replace("]", "").split(",");
        List<String> localNameList = new ArrayList<>();
        for (String localName : localNameArr) {
            localNameList.add(localName.replace("\"", ""));
        }
        speekItem.setLocalNameList(localNameList);

        String shortNames = jsonObject.getString("ShortName");
        String[] shortNameArr = shortNames.replace("[", "").replace("]", "").split(",");
        List<String> shortNameList = new ArrayList<>();
        for (String shortName : shortNameArr) {
            shortNameList.add(shortName.replace("\"", ""));
        }
        speekItem.setShortNameList(shortNameList);
        speekList.add(speekItem);

        if (speekInfo.length() > itemIndex + 1) {
            parseItem(speekList, speekInfo.substring(itemIndex + 1));
        }
    }

    private static String getSpeekInfo() {
//        return decodeUnicode(MyApplication.getInstance().getResources().getString(R.string.speek_text));
        return decodeUnicode(speek);
    }

    private static String decodeUnicode(String unicodeStr) {
        StringBuilder ret = new StringBuilder();
        int len = unicodeStr.length();
        for (int i = 0; i < len; i++) {
            char ch = unicodeStr.charAt(i);
            if (ch == '\\') {
                ch = unicodeStr.charAt(++i);
                if (ch == 'u') {
                    int code = 0;
                    for (int j = 0; j < 4; j++) {
                        ch = unicodeStr.charAt(++i);
                        switch (ch) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                code = (code << 4) + (ch - '0');
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                code = (code << 4) + 10 + (ch - 'a');
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                code = (code << 4) + 10 + (ch - 'A');
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx  encoding.");
                        }
                    }
                    ret.append((char) code);
                } else {
                    if (ch >= '0' && ch <= '7') {
                        int code = ch - '0';
                        if (i + 1 < len && unicodeStr.charAt(i + 1) >= '0' && unicodeStr.charAt(i + 1) <= '7') {
                            code = (code << 3) + (unicodeStr.charAt(++i) - '0');
                            if (i + 1 < len && unicodeStr.charAt(i + 1) >= '0' && unicodeStr.charAt(i + 1) <= '7') {
                                code = (code << 3) + (unicodeStr.charAt(++i) - '0');
                            }
                        }
                        ret.append((char) code);
                    } else {
                        ret.append(ch);
                    }
                }
            } else {
                ret.append(ch);
            }
        }
        return ret.toString();
    }
}
