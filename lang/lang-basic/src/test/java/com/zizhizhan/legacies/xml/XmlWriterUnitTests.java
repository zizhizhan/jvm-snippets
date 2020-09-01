package com.zizhizhan.legacies.xml;

import com.zizhizhan.legacy.xml.XmlWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class XmlWriterUnitTests {

    static private final String XML_ELEMENT_CHANGELOGCOMMENT = "changeLogComment";
    static private final String XML_ELEMENT_CHANGELOGLIST = "changeLogList";
    static private final String XML_ELEMENT_CHANGELOG = "changeLog";
    static private final String XML_ELEMENT_APPLICATION = "application";
    static private final String XML_ELEMENT_ATTRIBUTE = "attribute";
    static private final String XML_ELEMENT_TREE = "tree";
    static private final String XML_ELEMENT_TREENODE = "treenode";
    static private final String XML_ELEMENT_SETTING = "setting";
    static private final String XML_ELEMENT_SCHEMA = "schema";
    static private final String XML_ELEMENT_STRIPINGLIST = "stripingAttributeList";
    static private final String XML_ELEMENT_STRIPINGATTRIBUTE = "stripingAttribute";
    static private final String XML_ELEMENT_SETTINGNODELIST = "settingNodeList";
    static private final String XML_ELEMENT_SETTINGFAMILY = "settingFamily";
    static private final String XML_ELEMENT_SETTINGVALUE = "settingValue";
    static private final String XML_ELEMENT_SCHEMAMAPENTRY = "schemaMapEntry";
    static private final String XML_ATTRIBUTE_NAME = "name";
    static private final String XML_ATTRIBUTE_DESCRIPTION = "description";
    static private final String XML_ATTRIBUTE_VALIDATIONNAME = "validationRuleName";
    static private final String XML_ATTRIBUTE_DEFAULTTREENAME = "defaultTreeName";
    static private final String XML_ATTRIBUTE_GUID = "guid";
    static private final String XML_ATTRIBUTE_CONFIGTABLENAME = "configTableName";
    static private final String XML_ATTRIBUTE_VALUE = "value";
    static private final String XML_ATTRIBUTE_APPLICATIONNAME = "applicationName";
    static private final String XML_ATTRIBUTE_APPLICATIONVERSION = "applicationVersion";
    static private final String XML_ATTRIBUTE_FAMILYNAME = "familyName";
    static private final String XML_ATTRIBUTE_SCHEMANAME = "schemaName";

    private static Writer out;
    private static XmlWriter writer;

    static {

        out = new StringWriter();
        writer = new XmlWriter(out);

    }

    public static void main(String[] args) {
        //writeConfiguration();

        //System.exit(0);
        writer.beginElement("root");
        writer.beginBody(false);
        writer.beginElement("root1");
        writer.beginBody(false);
        writer.beginElement("root2");
        writer.addAttribute("name", "value");
        writer.beginBody(false);
        writer.beginElement("root3");
        writer.addAttribute("name", "value");
        writer.endElement();
        writer.endElement();
        writer.beginElement("root4");
        writer.beginBody(false);
        writer.beginElement("root5");
        writer.endElement();
        writer.endElement();
        writer.endElement();
        writer.endElement();

        System.out.println(out.toString());
    }

    public static void writeConfiguration() {
        writer.beginElement("configuration-data");
        writer.beginBody(false);

        wirteComment();
        wirteApplication();
        writeFamily();
        wirteAttribute();
        writeSchema();
        writeSchemaMap();

        writer.endElement();

        System.out.println(out.toString());
    }

    private static void wirteComment() {
        List<String> changeLogGuidList = new ArrayList<String>();
        changeLogGuidList.add("log1");
        changeLogGuidList.add("log2");
        changeLogGuidList.add("log3");
        changeLogGuidList.add("log4");
        changeLogGuidList.add("log5");
        changeLogGuidList.add("log6");

        writer.beginElement(XML_ELEMENT_CHANGELOGCOMMENT);
        writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION, "description");
        writer.endElement();

        writer.beginElement(XML_ELEMENT_CHANGELOGLIST);
        writer.beginBody(false);
        for (String changeLogGuid : changeLogGuidList) {
            writer.beginElement(XML_ELEMENT_CHANGELOG);
            writer.addAttribute(XML_ATTRIBUTE_GUID, changeLogGuid);
            writer.endElement();
        }
        writer.endElement();
    }

    private static void wirteApplication() {
        writer.beginElement(XML_ELEMENT_APPLICATION);
        writer.addAttribute(XML_ATTRIBUTE_NAME, "application.getDisplayName");
        writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION,
                "application.getDescription");
        writer.endElement();
    }

    private static void writeFamily() {
        Map<String, Setting> settingMap = new HashMap<String, Setting>();
        settingMap.put("setting1", new Setting("displayName1", "description1", "value1", "validation1"));
        settingMap.put("setting2", new Setting("displayName2", "description2", "value2", "validation2"));
        settingMap.put("setting3", new Setting("displayName3", "description3", "value3", "validation3"));

        writer.beginElement(XML_ELEMENT_SETTINGFAMILY);
        writer.addAttribute(XML_ATTRIBUTE_NAME, "family.getDisplayName");
        writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION, "family.getDescription");
        writer.beginBody(false);
        for (Map.Entry<String, Setting> mapEntry : settingMap.entrySet()) {
            Setting setting = mapEntry.getValue();
            writer.beginElement(XML_ELEMENT_SETTING);
            writer.addAttribute(XML_ATTRIBUTE_NAME, setting.getDisplayName());
            writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION, setting.getDescription());
            writer.addAttribute("default", setting.getDefaultValue());
            writer.addAttribute(XML_ATTRIBUTE_VALIDATIONNAME, setting.getValidationRuleName());
            writer.endElement();
        }
        writer.endElement();
    }

    private static void wirteAttribute() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("childName1", "parentName1");
        map.put("childName2", "parentName2");
        map.put("childName3", "parentName3");
        Map<String, AttributeTree> attributeTreeMap = new HashMap<String, AttributeTree>();
        attributeTreeMap.put("setting1", new AttributeTree("displayName1", "description1", map));
        attributeTreeMap.put("setting2", new AttributeTree("displayName2", "description2", map));
        attributeTreeMap.put("setting3", new AttributeTree("displayName3", "description3", map));

        writer.beginElement(XML_ELEMENT_ATTRIBUTE);
        writer.addAttribute(XML_ATTRIBUTE_NAME, "attribute.getDisplayName");

        // Attributes do not have a description.
        writer.addAttribute(XML_ATTRIBUTE_VALIDATIONNAME, "attribute.getValidationRuleName()");
        writer.addAttribute(XML_ATTRIBUTE_DEFAULTTREENAME, "attribute.getDefaultTreeName()");
        writer.beginBody(false);
        for (Map.Entry<String, AttributeTree> mapEntry : attributeTreeMap.entrySet()) {
            AttributeTree atree = mapEntry.getValue();

            writer.beginElement(XML_ELEMENT_TREE);
            writer.addAttribute(XML_ATTRIBUTE_NAME, atree.getDisplayName());
            writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION, atree.getDescription());
            writer.beginBody(false);

            for (Map.Entry<String, String> entry : atree.getMap().entrySet()) {
                String childName = entry.getKey();
                String parentName = entry.getValue();

                if (null != parentName) {
                    writer.beginElement(XML_ELEMENT_TREENODE);
                    writer.addAttribute(XML_ATTRIBUTE_NAME, parentName);
                    writer.beginBody(false);
                    writer.beginElement(XML_ELEMENT_TREENODE);
                    writer.addAttribute(XML_ATTRIBUTE_NAME, childName);
                    writer.endElement();
                    writer.endElement();
                }
            }
            writer.endElement();
        }
        writer.endElement();
    }

    private static void writeSchema() {
        List<SchemaAttribute> schemaAttributeList = new ArrayList<SchemaAttribute>();
        schemaAttributeList.add(new SchemaAttribute("attributeName1", "attributeTree1"));
        schemaAttributeList.add(new SchemaAttribute("attributeName2", "attributeTree2"));
        schemaAttributeList.add(new SchemaAttribute("attributeName3", "attributeTree3"));

        Map<String, Object> objectMap = new HashMap<String, Object>();
        ProviderWorker provider = new ProviderWorker();
        provider.setName("settingFamily");
        provider.setSettingValue("settingName1", "settingValue1");
        provider.setSettingValue("settingName2", "settingValue2");
        provider.setSettingValue("settingName3", "settingValue3");
        objectMap.put("[nodeName1]familyName1", provider);
        objectMap.put("[nodeName2]familyName2", provider);
        objectMap.put("[nodeName3]familyName3", provider);

        writer.beginElement(XML_ELEMENT_SCHEMA);
        writer.addAttribute(XML_ATTRIBUTE_NAME, "schema.getDisplayName()");
        writer.addAttribute(XML_ATTRIBUTE_DESCRIPTION, "schema.getDescription()");
        writer.addAttribute(XML_ATTRIBUTE_CONFIGTABLENAME, "schema.getConfigTableName()");
        writer.beginBody(false);
        writer.beginElement(XML_ELEMENT_STRIPINGLIST);
        writer.beginBody(false);
        String[] attributeNameArray = new String[6];
        int attributeCount = 0;
        for (SchemaAttribute sa : schemaAttributeList) {
            attributeNameArray[attributeCount++] = sa.getAttributeName();
            writer.beginElement(XML_ELEMENT_STRIPINGATTRIBUTE);
            writer.addAttribute(XML_ATTRIBUTE_NAME, sa.getAttributeName());
            writer.addAttribute(XML_ELEMENT_TREE, sa.getAttributeTree());
            writer.endElement();
        }
        writer.endElement();

        writer.beginElement(XML_ELEMENT_SETTINGNODELIST);
        writer.beginBody(false);
        for (Map.Entry<String, Object> mapEntry : objectMap.entrySet()) {
            String nodeName = mapEntry.getKey();

            if (!mapEntry.getValue().getClass().equals(ProviderWorker.class)) {
                continue;
            }
            ProviderWorker worker = (ProviderWorker) mapEntry.getValue();

            int index1 = nodeName.indexOf('[');
            int index2 = nodeName.indexOf(']');
            String attributeString = nodeName.substring(index1 + 1, index2);

            String[] attributeArrayRaw = attributeString.split(", ");
            String[] attributeArray = new String[6];
            for (int i = 0; i < attributeArray.length; i++) {
                attributeArray[i] = extractAttribute(attributeArrayRaw, i);
            }
            String familyName = nodeName.substring(index2 + 1, nodeName.length());

            writer.beginElement("settingNode");
            for (int i = 0; i < attributeArray.length; i++) {
                if (null != attributeArray[i] && !"null".equals(attributeArray[i])) {
                    writer.addAttribute(attributeNameArray[i], attributeArray[i]);
                }
            }
            writer.beginBody(false);
            writer.beginElement(XML_ELEMENT_SETTINGFAMILY);
            writer.addAttribute(XML_ATTRIBUTE_NAME, familyName);
            writer.beginBody(false);
            for (Map.Entry<String, String> entry : worker.getSettingMap().entrySet()) {
                writer.beginElement(XML_ELEMENT_SETTINGVALUE);
                writer.addAttribute(XML_ATTRIBUTE_NAME, entry.getKey());
                writer.addAttribute(XML_ATTRIBUTE_VALUE, entry.getValue());
                writer.endElement();
            }
            writer.endElement();
            writer.endElement();

        }
        writer.endElement();
        writer.endElement();
    }

    private static void writeSchemaMap() {
        List<SchemaMapEntry> schemaMapEntryList = new ArrayList<SchemaMapEntry>();
        schemaMapEntryList.add(new SchemaMapEntry("applicationName1",
                "application version 1.2", "familyName1", "schemaName1"));
        schemaMapEntryList.add(new SchemaMapEntry("applicationName2",
                "application version 1.4", "familyName2", "schemaName2"));
        schemaMapEntryList.add(new SchemaMapEntry("applicationName3",
                "application version 1.7", "familyName3", "schemaName3"));

        for (SchemaMapEntry entry : schemaMapEntryList) {
            // String familyName = mapEntry.getKey();

            writer.beginElement(XML_ELEMENT_SCHEMAMAPENTRY);
            writer.addAttribute(XML_ATTRIBUTE_APPLICATIONNAME, entry.getApplicationName());
            writer.addAttribute(XML_ATTRIBUTE_APPLICATIONVERSION, entry.getApplicationVersion());
            writer.addAttribute(XML_ATTRIBUTE_FAMILYNAME, entry.getFamilyName());
            writer.addAttribute(XML_ATTRIBUTE_SCHEMANAME, entry.getSchemaName());
            writer.endElement();
        }
    }


    private static String extractAttribute(String[] attributeArray, int i) {
        String value;

        if (i < attributeArray.length) {
            value = attributeArray[i];
            if (null == value) {
                value = "Root";
            } else {
                value = value.trim();
            }
            if ("".equals(value)) {
                value = "Root"; // As per database requirement.
            }
        } else {
            value = null;
        }

        return value;
    }

    private static class SchemaAttribute {
        private String attributeName;
        private String attributeTree;

        public SchemaAttribute() {
        }

        public SchemaAttribute(String name, String tree) {
            attributeName = name;
            attributeTree = tree;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeTree() {
            return attributeTree;
        }

        public void setAttributeTree(String attributeTree) {
            this.attributeTree = attributeTree;
        }

    }

    private static class Setting {
        private String displayName;
        private String description;
        private String defaultValue;
        private String validationRuleName;

        public Setting() {
        }

        public Setting(String displayName, String description, String value, String validation) {
            this.displayName = displayName;
            this.description = description;
            this.defaultValue = value;
            this.validationRuleName = validation;
        }

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getValidationRuleName() {
            return validationRuleName;
        }

        public void setValidationRuleName(String validationRuleName) {
            this.validationRuleName = validationRuleName;
        }
    }

    private static class AttributeTree {
        private String displayName;
        private String description;
        private Map<String, String> map;

        public AttributeTree() {

        }

        public AttributeTree(String description, String name, Map<String, String> map) {
            this.description = description;
            displayName = name;
            this.map = map;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public void setMap(Map<String, String> map) {
            this.map = map;
        }
    }


    private static class ProviderWorker {

        private String name;
        private Map<String, String> map;
        private boolean isPopulating;

        public ProviderWorker() {
            map = null;
        }

        public String getSettingValue(String settingName) {
            if (null != map) {
                return map.get(settingName);
            } else {
                return null;
            }
        }

        public Map<String, String> getSettingMap() {
            return map;
        }

        public void setSettingMap(Map<String, String> map) {
            this.map = map;
        }

        public void setSettingValue(String settingName, String settingValue) {
            if (null == map) {
                map = new HashMap<String, String>();
            }

            String previousValue = map.put(settingName, settingValue);
            if (null != previousValue && !previousValue.equals(settingValue)) {
                log.warn("Duplicate value for setting " + settingName
                        + "; previousValue = " + previousValue
                        + " <>  newValue = " + settingValue);
            }
        }

        public int getSettingCount() {
            if (null != map) {
                return map.size();
            } else {
                return -1;
            }
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return name;
        }

        public boolean getIsPopulating() {
            return isPopulating;
        }

        public void setIsPopulating(boolean isPopulating) {
            this.isPopulating = isPopulating;
        }

    }

    private static class SchemaMapEntry {
        private String applicationName = null;
        private String applicationGuid = null;
        private String applicationVersion = null;
        private String familyName = null;
        private String schemaName = null;
        private String schemaGuid = null;


        public SchemaMapEntry(String name, String version, String name2, String name3) {
            applicationName = name;
            applicationVersion = version;
            familyName = name2;
            schemaName = name3;
        }

        public String getApplicationGuid() {
            return applicationGuid;
        }

        public void setApplicationGuid(String applicationGuid) {
            this.applicationGuid = applicationGuid;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getApplicationVersion() {
            return applicationVersion;
        }

        public void setApplicationVersion(String applicationVersion) {
            this.applicationVersion = applicationVersion;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getSchemaGuid() {
            return schemaGuid;
        }

        public void setSchemaGuid(String schemaGuid) {
            this.schemaGuid = schemaGuid;
        }

        public String getSchemaName() {
            return schemaName;
        }

        public void setSchemaName(String schemaName) {
            this.schemaName = schemaName;
        }
    }


}
