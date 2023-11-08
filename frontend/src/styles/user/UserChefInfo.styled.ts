import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

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
  max-width: 100%;
  height: 20px;
  border-radius: 10px;
  background-color: ${palette.textSecondary};
`

export const GradeList = styled.div`
  display: flex;
  justify-content: space-between;
`

export const Grade = styled.span`
  font-weight: bold;
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

export const ReviewCardList = styled.ul<{ $isRow: boolean }>`
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding-left: 0;
  list-style: none;
  ${breakpoints.large} {
    flex-direction: ${props => (props.$isRow ? 'column' : 'row')};
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
  gap: 10px;
`
export const ReviewImg = styled.img`
  width: 40px;
  height: 40px;
  border-radius: 100%;
  background-color: yellowgreen;
  margin-right: 6px;
  object-fit: cover;
`

export const ReviewNickName = styled.span`
  display: flex;
  align-items: center;
`

export const ReviewContent = styled.p`
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const NoReviewCard = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 140px;
  border-radius: 10px;
  color: ${palette.divider};
  box-shadow: 0px 2px 6px 2px rgba(89, 97, 104, 0.1);
`

export const EtcComment = styled.p`
  text-align: center;
  color: ${palette.divider};
`
