import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const MyProfileContainer = styled.div`
  padding: 5%;

  ${breakpoints.large} {
    padding: 2%;
  }
`

export const NormalInfoContainer = styled.div`
  display: flex;
  gap: 30px;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 60px;
  ${breakpoints.large} {
    justify-content: start;
  }
`

export const UserContainer = styled.div`
  display: flex;
  gap: 20px;
  align-items: center;
`

export const UserImgContainer = styled.div`
  position: relative;
`

export const UserImg = styled.img`
  border-radius: 100%;
  width: 60px;
  height: 60px;
  background-color: yellowgreen;
`

export const UserIsCooker = styled.div`
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
  width: 70px;
  height: 20px;
  border-radius: 10px;
  font-size: 1.2rem;
  line-height: 1.2rem;
  color: ${palette.white};
  background-color: ${palette.main};
`
export const UserNickname = styled.span`
  font-weight: bold;
  font-size: 2rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const LikeDiv = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
`
export const MyGradeDiv = styled.div`
  margin-bottom: 30px;
`

export const MyGradeP = styled.p`
  font-size: 1.8rem;
  font-weight: bold;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const Bar = styled.div`
  position: relative;
  width: 100%;
  height: 20px;
  border-radius: 10px;
  background-color: ${palette.divider};
`

export const ExpBar = styled.div<{ $cookerExp: number }>`
  position: absolute;
  top: 0;
  left: 0;
  width: ${props => props.$cookerExp}%;
  height: 20px;
  border-radius: 10px;
  background-color: ${palette.textSecondary};
`

export const GradeList = styled.div`
  display: flex;
  justify-content: space-between;
`

export const ReviewContainer = styled.div`
  margin-bottom: 30px;
`

export const ReviewIntro = styled.div`
  display: flex;
  justify-content: space-between;
`

export const ReviewTitle = styled.p`
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const ReviewMoreBtn = styled.button`
  font-size: 2rem;
  padding: 0 10px;
`

export const ReviewCardList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-left: 0;
  list-style: none;
  ${breakpoints.large} {
    flex-direction: row;
  }
`

export const ReviewCard = styled.li`
  width: 100%;
  padding: 14px;
  list-style: none;
  border-radius: 10px;
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`

export const ReviewTopInfo = styled.div`
  display: flex;
  align-content: center;
  gap: 10px;
  margin-bottom: 10px;
`

export const ReviewAuthor = styled.div`
  display: flex;
  align-content: center;

  img {
    width: 40px;
    height: 40px;
    border-radius: 100%;
    background-color: yellowgreen;
    margin-right: 6px;
  }
  span {
    line-height: 40px;
  }
`

export const ReviewContent = styled.p`
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`
export const MyUploadContainer = styled.div`
  margin-bottom: 30px;
`

export const MyUploadIntro = styled.div`
  display: flex;
  justify-content: space-between;
`
export const MyUploadTitle = styled.span`
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const UploadMoreBtn = styled.button`
  font-size: 2rem;
  padding: 0 10px;
`

export const MyRecipeList = styled.div`
  display: grid;
  justify-items: center;
  row-gap: 1rem;
  grid-template-columns: 1fr 1fr;
  ${breakpoints.large} {
    row-gap: 0;
    grid-template-columns: 1fr 1fr 1fr 1fr;
  }
`

export const MyReviewedContainer = styled.div`
  margin-bottom: 30px;
`

export const MyReviewedInto = styled.div`
  display: flex;
  justify-content: space-between;
`

export const MyReviewedTitle = styled.span`
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const ReviewedMoreBtn = styled.button`
  font-size: 2rem;
  padding: 0 10px;
`

export const ReviewedList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`

export const SpacingDiv = styled.div`
  height: 93px;
  ${breakpoints.large} {
    display: none;
  }
`
