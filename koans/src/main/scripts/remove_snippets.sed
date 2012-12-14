:t
/SNIPPET_START/,/SNIPPET_END/ {   
   /SNIPPET_END/!{         
      $!{          
         N;        
         bt
      }            
   }               
   /.*/d;       
}
