package edu.upenn.cis.cis555.youtube;

import java.util.ArrayList;

import rice.p2p.commonapi.Message;
import rice.p2p.commonapi.NodeHandle;

public class IndexPutMsg implements Message{

       private static final long serialVersionUID = 1L;

       NodeHandle senderNodeHandle;
       String token;
       ArrayList<Posting> postingList;
       int roundNo;

       public IndexPutMsg(String token, ArrayList<Posting> postingList, int roundNo, NodeHandle senderNodeHandle){
               this.token = token;
               this.postingList = postingList;
               this.roundNo = roundNo;
               this.senderNodeHandle = senderNodeHandle;
       }

       public String getToken(){
               return token;
       }

       public int getRoundNo(){
               return roundNo;
       }

       public NodeHandle getSenderNodeHandle(){
               return senderNodeHandle;
       }

       public ArrayList<Posting> getPostingList(){
               return postingList;
       }

       public int getPriority() {
               return 0;
       }
}