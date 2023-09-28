import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const RequestContainer = styled.div`
  display: flex;
  flex-direction: column;
  padding: 0 5%;
  width: 100%;
  margin: 0 auto;
`

export const RequestHeader = styled.h2`
  display: block;
  width: 100%;
  text-align: center;
  padding-bottom: 1%;
  margin-bottom: 3%;
  font-size: 2.4rem;
  border-bottom: 1px solid ${palette.divider};
`

export const RequestFormList = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 30px;
  padding-left: 0;
`

export const ElementTitle = styled.p`
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 10px;
`
export const IngredientsBoxContainer = styled.div`
  display: flex;
  gap: 20px;
`

export const IngredientsBox = styled.div`
  padding: 10px 14px;
  border: 1px solid ${palette.divider};
  border-radius: 10px;
`

export const TaggedRecipe = styled.div`
  width: 200px;
  height: 240px;
  border-radius: 10px;
  border: 1px solid ${palette.divider};
`

export const TaggedImg = styled.img`
  width: 100%;
  height: 50%;
  background-color: teal;
  border-radius: 10px 10px 0 0;
`

export const TaggedInfo = styled.div`
  height: 50%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 10px;
`

export const TaggedTopInfo = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
`
export const TaggedTitle = styled.span`
  font-weight: bold;
`

export const TaggedLike = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  span {
    font-size: 1.2rem;
  }
`

export const TaggedBottomInfo = styled.p`
  font-size: 1.4rem;
`

export const ButtonList = styled.div`
  margin: 50px 0 145px;
  display: flex;
  justify-content: center;
  gap: 10px;

  ${breakpoints.large} {
    margin: 50px 0;
  }
`

export const RejectButton = styled.button`
  width: 120px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background-color: ${palette.divider};
  color: ${palette.textPrimary};
  font-weight: 600;
  border-radius: 5px;
`

export const AcceptButton = styled.button`
  width: 120px;
  height: 36px;
  line-height: 36px;
  text-align: center;
  background-color: ${palette.main};
  color: ${palette.white};
  font-weight: 600;
  border-radius: 5px;
`
