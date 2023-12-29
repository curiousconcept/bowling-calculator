//package com.bowling.game.windowstrategy.frame;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//
//public class StandardGameFrame extends LinkedFrame<StandardGameFrame> {
//
//
//
//    private Integer firstAttempt;
//    private Integer secondAttempt;
//
//    private BonusState.BonusType bonus = BonusState.BonusType.NONE;
//
//    private int totalScore = 0;
//    private int bonusScore = 0;
//
//    private int index;
//
//    public StandardGameFrame(int index, int totalScore) {
//        this.totalScore = totalScore;
//        this.index = index;
//
//    }
//
//    public void roll(int pins){
//
//        if (getFirstAttempt().isEmpty()) {
//
//            firstAttempt = pins;
//
//            if (isStrike(firstAttempt)) {
//                bonus = BonusState.BonusType.STRIKE;
//                return;
//            }
//
//        }
//
//        secondAttempt = pins;
//
//        if(isSpare()) {
//            bonus = BonusState.BonusType.SPARE;
//        }
//    }
//
//    public Optional<Integer> getFirstAttempt() {
//        return Optional.ofNullable(firstAttempt);
//    }
//
//    public boolean isBonusBreaker(){
//       return Stream.of(Optional.ofNullable(firstAttempt).filter(score -> score == 0).isPresent(),
//                        getSecondAttempt().isPresent())
//              .allMatch(aBoolean -> aBoolean );
//    }
//
//    public Optional<Integer> getSecondAttempt() {
//        return Optional.ofNullable(secondAttempt);
//    }
//
//    public boolean hasBonus(){
//        return bonus != BonusState.BonusType.NONE;
//    }
//
//    private static boolean isStrike(int pins) {
//        return pins == 10;
//    }
//
//    private boolean isSpare() {
//        return firstAttempt + secondAttempt == 10;
//    }
//
//    public int updateWithBonusSelfAndWithTotalNext(StandardGameFrame nextGameFrame){
//        bonusScore+= pins;
//
//        return totalScore + firstAttempt + secondAttempt + bonusScore;
//    }
//
//    public boolean resolveBonusFromWindow(List<StandardGameFrame> framesWindowList) {
//
//
//    }
//
//
//    public BonusState.BonusType getBonus() {
//        return bonus;
//    }
//
//    private boolean hasTwoBowls(){
//        return this.bonus != BonusState.BonusType.STRIKE;
//    }
//
//    public int getFrameScore(){
//        return firstAttempt+secondAttempt;
//    }
//
//
//    public void updateFrameScore(int baseScore){
//
//
//        // events.notify (frameScoreUpdatedEvent(frameIdWithChangedScore, score))
//    }
//
//    public int getIndex() {
//        return index;
//    }
//
//
//
//    @Override
//    int getTotalScore(int newTotal) {
//        totalScore = newTotal;
//        return totalScore + firstAttempt + secondAttempt + bonusScore;
//    }
//
//    private static class BonusState {
//
//        private int gameFrameAlreadyInspected = BonusState.NOTHING_EXPECTED_YET;
//
//        private static final int NOTHING_EXPECTED_YET = -1;
//
//        private final BonusType bonusType;
//
//        private int bonusPoints;
//
//        public BonusState(BonusType bonus) {
//            this.bonusType = bonus;
//        }
//
//        public boolean resolveBonusWithNextFrame(StandardGameFrame nextGameFrame){
//
//            switch (bonusType) {
//                case SPARE -> {
//                    bonusPoints = nextGameFrame.getFirstAttempt().orElseThrow(IllegalArgumentException::new);
//                    return true;
//                }
//                case STRIKE -> {
//
//                    // case of most adjacent frame bonus which contains two bowls (e.g. simple score, no strike)
//                    if (gameFrameAlreadyInspected == NOTHING_EXPECTED_YET && nextGameFrame.hasTwoBowls()) {
//                        bonusPoints = nextGameFrame.getFrameScore();
//                        return true;
//                    }
//
//                    if(gameFrameAlreadyInspected == nextGameFrame.getIndex()) {
//                        return false;
//                    }
//
//                    gameFrameAlreadyInspected = nextGameFrame.getIndex();
//                    bonusPoints += nextGameFrame.getFirstAttempt().orElseThrow(IllegalArgumentException::new);
//                    return true;
//                }
//
//                default -> throw new IllegalStateException("Unknown bonus type: "+ bonusType);
//            }
//        }
//
//
//        public enum BonusType {
//            SPARE, STRIKE, NONE
//        }
//    }
//
//}
