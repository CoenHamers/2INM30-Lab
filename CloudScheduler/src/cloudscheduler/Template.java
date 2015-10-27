/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cloudscheduler;

/**
 *
 * @author Pieter
 */
public class Template {
    public static String Qcow()
    {
        return "NAME   = Centos5-disk\n" +
"CPU    = 0.5\n" +
"MEMORY = 400\n" +
"\n" +
"OS     = [\n" +
"  arch = x86_64\n" +
"]\n" +
"\n" +
"DISK   = [\n" +
"  # IMAGE_ID=7,\n" +
"  # using the optimized qcow2 transfer method:\n" +
"  IMAGE_ID=35,\n" +
"  DRIVER=qcow2\n" +
"]\n" +
"\n" +
"NIC    = [\n" +
"  # NETWORK = \"Small network\"\n" +
"  # OpenNebula API change: now refer to NETWORK ID:\n" +
"  NETWORK_ID=0\n" +
"]\n" +
"\n" +
"GRAPHICS = [\n" +
"  TYPE    = \"vnc\",\n" +
"  LISTEN  = \"0.0.0.0\"\n" +
"]\n" +
"\n" +
"FEATURES = [\n" +
"  # Needed for graceful shutdown with KVM:\n" +
"  acpi=\"yes\"\n" +
"]\n" +
"\n" +
"RAW = [\n" +
"  type = \"kvm\",\n" +
"  data = \" <serial type='pty'> <source path='/dev/pts/3'/> <target port='1'/> </serial>\"\n" +
"]\n" +
"\n" +
"CONTEXT = [\n" +
"  hostname   = \"$NAME\",\n" +
"  # OpenNebula API change: now refer to NETWORK ID:\n" +
"  dns        = \"$NETWORK[DNS,     NETWORK_ID=0]\",\n" +
"  gateway    = \"$NETWORK[GATEWAY, NETWORK_ID=0]\",\n" +
"  netmask    = \"$NETWORK[NETMASK, NETWORK_ID=0]\",\n" +
"  files      = \"/cm/shared/package/OpenNebula/current/srv/cloud/configs/centos-5/init.sh /var/scratch/cld1522/OpenNebula/id_dsa.pub\",\n" +
"  target     = \"hdc\", \n" +
"  root_pubkey = \"id_dsa.pub\",\n" +
"  username    = \"opennebula\",\n" +
"  user_pubkey = \"id_dsa.pub\"\n" +
"]";
    }
    
    public static String InfoXml()
    {
        return "<VM><ID>40889</ID><UID>343</UID><GID>1</GID><UNAME>cld1522</UNAME><GNAME>users</GNAME><NAME>Centos5-disk</NAME><PERMISSIONS><OWNER_U>1</OWNER_U><OWNER_M>1</OWNER_M><OWNER_A>0</OWNER_A><GROUP_U>0</GROUP_U><GROUP_M>0</GROUP_M><GROUP_A>0</GROUP_A><OTHER_U>0</OTHER_U><OTHER_M>0</OTHER_M><OTHER_A>0</OTHER_A></PERMISSIONS><LAST_POLL>1445893777</LAST_POLL><STATE>3</STATE><LCM_STATE>3</LCM_STATE><RESCHED>0</RESCHED><STIME>1443692246</STIME><ETIME>0</ETIME><DEPLOY_ID>one-40889</DEPLOY_ID><MEMORY>434776</MEMORY><CPU>12</CPU><NET_TX>1536973</NET_TX><NET_RX>2182270</NET_RX><TEMPLATE><CONTEXT><DISK_ID><![CDATA[1]]></DISK_ID><DNS><![CDATA[10.141.3.254]]></DNS><FILES><![CDATA[/cm/shared/package/OpenNebula/current/srv/cloud/configs/centos-5/init.sh /var/scratch/cld1522/OpenNebula/id_dsa.pub]]></FILES><GATEWAY><![CDATA[10.141.3.254]]></GATEWAY><HOSTNAME><![CDATA[Centos5-disk]]></HOSTNAME><NETMASK><![CDATA[255.255.255.0]]></NETMASK><ROOT_PUBKEY><![CDATA[id_dsa.pub]]></ROOT_PUBKEY><TARGET><![CDATA[hdc]]></TARGET><USERNAME><![CDATA[opennebula]]></USERNAME><USER_PUBKEY><![CDATA[id_dsa.pub]]></USER_PUBKEY></CONTEXT><CPU><![CDATA[0.5]]></CPU><DISK><CLONE><![CDATA[YES]]></CLONE><DATASTORE><![CDATA[qcow2]]></DATASTORE><DATASTORE_ID><![CDATA[100]]></DATASTORE_ID><DEV_PREFIX><![CDATA[hd]]></DEV_PREFIX><DISK_ID><![CDATA[0]]></DISK_ID><DRIVER><![CDATA[qcow2]]></DRIVER><IMAGE><![CDATA[CentOS-5.4 Public Desktop, using qcow2 format on qcow2 datastore]]></IMAGE><IMAGE_ID><![CDATA[35]]></IMAGE_ID><READONLY><![CDATA[NO]]></READONLY><SAVE><![CDATA[NO]]></SAVE><SOURCE><![CDATA[/cm/shared/package/OpenNebula/3.4.1//srv/cloud/one/var/datastores/100/493bcfe30913962bb26ef0c8014ac1e6]]></SOURCE><TARGET><![CDATA[hda]]></TARGET><TM_MAD><![CDATA[qcow2]]></TM_MAD><TYPE><![CDATA[FILE]]></TYPE></DISK><FEATURES><ACPI><![CDATA[yes]]></ACPI></FEATURES><GRAPHICS><LISTEN><![CDATA[0.0.0.0]]></LISTEN><PORT><![CDATA[50889]]></PORT><TYPE><![CDATA[vnc]]></TYPE></GRAPHICS><MEMORY><![CDATA[400]]></MEMORY><NAME><![CDATA[Centos5-disk]]></NAME><NIC><BRIDGE><![CDATA[br0]]></BRIDGE><IP><![CDATA[10.141.3.171]]></IP><MAC><![CDATA[02:00:0a:8d:03:ab]]></MAC><NETWORK><![CDATA[Small network]]></NETWORK><NETWORK_ID><![CDATA[0]]></NETWORK_ID><VLAN><![CDATA[NO]]></VLAN></NIC><OS><ARCH><![CDATA[x86_64]]></ARCH></OS><RAW><DATA><![CDATA[ <serial type='pty'> <source path='/dev/pts/3'/> <target port='1'/> </serial>]]></DATA><TYPE><![CDATA[kvm]]></TYPE></RAW><VMID><![CDATA[40889]]></VMID></TEMPLATE><HISTORY_RECORDS><HISTORY><OID>40889</OID><SEQ>0</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1443692259</STIME><ETIME>1443693255</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>1443692259</PSTIME><PETIME>1443692259</PETIME><RSTIME>1443692259</RSTIME><RETIME>1443693255</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>1</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1443693305</STIME><ETIME>1443693771</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1443693305</RSTIME><RETIME>1443693771</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>2</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1443694069</STIME><ETIME>1444035390</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1443694069</RSTIME><RETIME>1444035390</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>3</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1444036772</STIME><ETIME>1444041229</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1444036772</RSTIME><RETIME>1444041229</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>4</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1445246878</STIME><ETIME>1445251723</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1445246878</RSTIME><RETIME>1445251723</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>5</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1445857766</STIME><ETIME>1445887277</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1445857766</RSTIME><RETIME>1445887277</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>2</REASON></HISTORY><HISTORY><OID>40889</OID><SEQ>6</SEQ><HOSTNAME>node320</HOSTNAME><HID>85</HID><STIME>1445887582</STIME><ETIME>0</ETIME><VMMMAD>vmm_kvm</VMMMAD><VNMMAD>dummy</VNMMAD><TMMAD>shared</TMMAD><DS_LOCATION>/cm/shared/package/OpenNebula/3.8.3/srv/cloud/one/var//datastores</DS_LOCATION><DS_ID>0</DS_ID><PSTIME>0</PSTIME><PETIME>0</PETIME><RSTIME>1445887582</RSTIME><RETIME>0</RETIME><ESTIME>0</ESTIME><EETIME>0</EETIME><REASON>0</REASON></HISTORY></HISTORY_RECORDS></VM>";
    }

}
