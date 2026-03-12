package lineage.world.object.magic;

import Fx.server.MJTemplate.MJProto.Models.SC_BUFFICON_NOTI;
import Fx.server.MJTemplate.MJProto.Models.SC_SKILL_DELAY_NOTI;
import lineage.bean.database.MonsterSkill;
import lineage.bean.database.Skill;
import lineage.bean.lineage.BuffInterface;
import lineage.network.packet.BasePacketPooling;
import lineage.network.packet.server.S_CharacterStat;
import lineage.network.packet.server.S_Message;
import lineage.network.packet.server.S_ObjectAction;
import lineage.network.packet.server.S_ObjectEffect;
import lineage.share.Lineage;
import lineage.world.controller.BuffController;
import lineage.world.controller.ChattingController;
import lineage.world.controller.SkillController;
import lineage.world.object.Character;
import lineage.world.object.object;
import lineage.world.object.instance.PcInstance;

public class Disease extends Magic {

    public Disease(Skill skill) {
        super(null, skill);
    }

    static synchronized public BuffInterface clone(BuffInterface bi, Skill skill, int time) {
        if (bi == null)
            bi = new Disease(skill);
        bi.setSkill(skill);
        bi.setTime(time);
        return bi;
    }
    
    @Override
    public void toBuffStart(object o) {
        o.setBuffDisease(true);
        
        if (o instanceof Character) {
            Character cha = (Character) o;
            
            // [수정 핵심] AC가 '안 좋아져야' 하므로 수치를 '더해야(+)' 합니다.
            // 리니지 AC는 낮을수록(마이너스일수록) 좋습니다.
            // 기존: cha.setDynamicAc(cha.getDynamicAc() - 12); (방어력 좋아짐)
            // 변경: cha.setDynamicAc(cha.getDynamicAc() + 12); (방어력 나빠짐)
            cha.setDynamicAc(cha.getDynamicAc() + 12); 
            
            // 명중률 감소 (-6)
            cha.setDynamicAddHit(cha.getDynamicAddHit() - 6);
            
            // 스탯 갱신
            cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
            
            // 공격당한거 알리기.
            o.toDamage(cha, 0, Lineage.ATTACK_TYPE_MAGIC);
        }
        
        toBuffUpdate(o);
    }

    @Override
    public void toBuffUpdate(object o) {
        if (o instanceof PcInstance) {
            PcInstance pc = (PcInstance) o;
            SC_BUFFICON_NOTI.on(pc, 69, this.getTime(), SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
        }
    }

    @Override
    public void toBuffStop(object o) {
        toBuffEnd(o);
    }

    @Override
    public void toBuffEnd(object o) {
        o.setBuffDisease(false);
        
        if (o instanceof Character) {
            Character cha = (Character) o;
            
            // [수정] 원상복구 (높였던 AC를 다시 뺌)
            cha.setDynamicAc(cha.getDynamicAc() - 12);
            
            // 원상복구 (떨어진 명중을 다시 높임)
            cha.setDynamicAddHit(cha.getDynamicAddHit() + 6);
            
            // 스탯 갱신
            cha.toSender(S_CharacterStat.clone(BasePacketPooling.getPool(S_CharacterStat.class), cha));
        }
        
        if (o instanceof PcInstance) {
            SC_BUFFICON_NOTI.on((PcInstance)o, 69, 0, SC_BUFFICON_NOTI.REMAINING_TYPE_SECONDS);
        }
    }
    
    // [일반 마법 사용 시]
    static public void init(Character cha, Skill skill, int object_id) {
        object o = null;
        if (object_id == cha.getObjectId())
            o = cha;
        else
            o = cha.findInsideList(object_id);

        if (o != null) {
            cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, Lineage.GFX_MODE_SPELL_NO_DIRECTION), true);
            
            if (cha instanceof PcInstance) {
                SC_SKILL_DELAY_NOTI.newInstance().setDurationMs(1100).send((PcInstance) cha);
            }
            
            if (SkillController.isMagic(cha, skill, true)) {
                Detection.onBuff(cha);
                if (SkillController.isFigure(cha, o, skill, true, false)) {
                    onBuff(o, skill, skill.getBuffDuration());
                    return;
                }
                cha.toSender(S_Message.clone(BasePacketPooling.getPool(S_Message.class), 280));
            }
        }
    }
    
    // [무기 발동용 - 수정됨]
    static public void init2(Character cha, Skill skill, int object_id) {
        object o = null;
        if (object_id == cha.getObjectId())
            o = cha;
        else
            o = cha.findInsideList(object_id);
            
        if (o != null) {
            Detection.onBuff(cha);
            
            // [중요] SkillController.isFigure는 마법 적중(MR 체크) 여부입니다.
            // true일 때만 onBuff가 실행되므로, 이때만 이펙트가 나옵니다.
            // 만약 무조건 걸리게 하고 싶다면 if문 조건을 지우고 바로 onBuff를 호출하세요.
            if (SkillController.isFigure(cha, o, skill, true, false)) {
                onBuff(o, skill, skill.getBuffDuration());
                return;
            }
        }
    }

    static public void onBuff(object o, Skill skill, int time) {
        // [핵심] 여기서 이펙트를 보내줍니다. (ItemSkill에선 0으로 처리했으므로 여기서만 나감)
        o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, skill.getCastGfx()), true);
        BuffController.append(o, Disease.clone(BuffController.getPool(Disease.class), skill, time));
    }
    
    /**
     * 몬스터용
     */
    static public void init(Character cha, object o, MonsterSkill ms, int action) {
        if (o != null) {
            Detection.onBuff(cha);

            if (action > 0)
                cha.toSender(S_ObjectAction.clone(BasePacketPooling.getPool(S_ObjectAction.class), cha, action), true);

            if (SkillController.isMagic(cha, ms, true) && SkillController.isFigure(cha, o, ms, false, false)) {
                o.toSender(S_ObjectEffect.clone(BasePacketPooling.getPool(S_ObjectEffect.class), o, ms.getCastGfx() < 1 ? ms.getSkill().getCastGfx() : ms.getCastGfx()), true);
                BuffController.append(o, Disease.clone(BuffController.getPool(Disease.class), ms.getSkill(), ms.getBuffDuration() < 1 ? ms.getSkill().getBuffDuration() : ms.getBuffDuration()));
            }
        }
    }
}