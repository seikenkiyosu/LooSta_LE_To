import java.util.Random;
import Agent.Agent_To;
import Interaction.Interaction_To;
import RandamPackage.*;

class Loosta_LE_To{
	public static final int Gridsize = 100;
	public static final int Roundnum = 1000000;
	
	public static final int s = 96;			//96�ȏ��3n�ȏ�
	public static final int n = 20;
	
	public static final int Vset = 10;	//vの速度の上限
	public static final int DistanceforInteraction = 5;	//interactionができる距離

	
	public static String RandomMethod = "Torus";	//Torus or RWP(Random Way Point)
	
	public static void main(String args[]){
		Random random = new Random();
		Agent_To agent[] = new Agent_To[n];
		int CTcounter = 0;
		int CT = 0, HT = 0;
		boolean HT_count_flag = false, CT_count_flag = true;
		
		for(int i=0; i<n; i++)
		agent[i] = new Agent_To(random.nextBoolean(), random.nextInt(Gridsize)+random.nextDouble(), random.nextInt(Gridsize)+random.nextDouble(), s);
		
		for(int i=0; i<Roundnum; i++){
			int leadercount=0;
//			int leaderid=0;
			//リーダの数をかぞえる
			for(int j=0; j<n; j++) if(agent[j].IsLeader()){ leadercount++; }
			
			//Holding Timeが終了したらぬける
			if(leadercount!=1 && HT_count_flag==true){ break; }
			
			if(leadercount==1){ 
						HT_count_flag = true;
						CT_count_flag = false;
					}
			
			if(HT_count_flag==true) HT++;
		
			int p, q;
			while(true){					//リーダが決定するまで
				p = random.nextInt(n);		//interactionをするagentをランダムで選択
				q = RandomWay_To.RandamPickNearAgent( p, n, agent, DistanceforInteraction);		//p�Ƌ���1�ȓ��ɂ���m�[�h�̒���(���id�̒Ⴂ)�m�[�h��q�ɑ��
				if(q != -1) { 	//pの周りにinteractionが可能なAgentが見つかったとき
					Interaction_To.interaction(agent[p], agent[q], s);	
					for(int j=0; j<n; j++) agent[j].Countdown();	//交流したagentのtimerをデクリメント
					break;						//次のラウンドへ
				}
				for(int j=0; j<n; j++){					//各Agentをランダムに移動させる
					agent[j].Vchange((random.nextInt(2*Vset)-Vset)+random.nextDouble() , (random.nextInt(2*Vset)-Vset)+random.nextDouble() );
					agent[j].ShiftPointForTorus(Gridsize);	//移動
				}
			}
			if(CT_count_flag==true) CT++;
			if(CT!=0 && HT!=0 && HT+CT+1 < CTcounter) break;	//HT���I����Ă瑦�I������������
		}
		System.out.println("CT = " + CT);
		System.out.println("HT = " + HT);
	}
}
